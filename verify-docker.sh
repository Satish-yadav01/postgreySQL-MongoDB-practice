#!/bin/bash

# Docker Container Verification and Testing Script
# This script verifies all containers are running and tests all API endpoints

# Don't exit on errors - we want to continue testing
set +e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Counters
PASSED=0
FAILED=0

# Function to print colored output
print_header() {
    echo -e "\n${CYAN}========================================${NC}"
    echo -e "${CYAN}$1${NC}"
    echo -e "${CYAN}========================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
    ((PASSED++))
}

print_error() {
    echo -e "${RED}✗${NC} $1"
    ((FAILED++))
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
check_prerequisites() {
    print_header "Checking Prerequisites"
    
    if command_exists docker; then
        print_success "Docker is installed"
    else
        print_error "Docker is not installed"
        exit 1
    fi
    
    if command_exists docker-compose || command_exists docker; then
        print_success "Docker Compose is installed"
    else
        print_error "Docker Compose is not installed"
        exit 1
    fi
    
    if command_exists curl; then
        print_success "curl is installed"
    else
        print_error "curl is not installed"
        exit 1
    fi
    
    if command_exists jq; then
        print_success "jq is installed (for pretty JSON output)"
    else
        print_warning "jq is not installed (JSON output won't be formatted)"
    fi
}

# Check Docker daemon
check_docker_daemon() {
    print_header "Checking Docker Daemon"
    
    if timeout 5 docker info > /dev/null 2>&1; then
        print_success "Docker daemon is running"
    else
        print_error "Docker daemon is not running or not responding"
        exit 1
    fi
}

# Check container status
check_containers() {
    print_header "Checking Container Status"
    
    # Check if containers are running
    if timeout 10 docker-compose ps 2>/dev/null | grep -q "Up"; then
        print_success "Docker Compose services are running"
    else
        print_error "Docker Compose services are not running"
        print_info "Run: docker-compose up -d"
        exit 1
    fi
    
    # Check PostgreSQL
    if timeout 5 docker-compose ps postgres 2>/dev/null | grep -q "Up.*healthy"; then
        print_success "PostgreSQL container is running and healthy"
    elif timeout 5 docker-compose ps postgres 2>/dev/null | grep -q "Up"; then
        print_warning "PostgreSQL container is running but not yet healthy"
    else
        print_error "PostgreSQL container is not running"
    fi
    
    # Check MongoDB
    if timeout 5 docker-compose ps mongodb 2>/dev/null | grep -q "Up.*healthy"; then
        print_success "MongoDB container is running and healthy"
    elif timeout 5 docker-compose ps mongodb 2>/dev/null | grep -q "Up"; then
        print_warning "MongoDB container is running but not yet healthy"
    else
        print_error "MongoDB container is not running"
    fi
    
    # Check Spring Boot App
    if timeout 5 docker-compose ps app 2>/dev/null | grep -q "Up.*healthy"; then
        print_success "Spring Boot application container is running and healthy"
    elif timeout 5 docker-compose ps app 2>/dev/null | grep -q "Up"; then
        print_warning "Spring Boot application is running but not yet healthy"
    else
        print_error "Spring Boot application container is not running"
    fi
    
    # Display container details
    echo ""
    print_info "Container Details:"
    timeout 10 docker-compose ps 2>/dev/null || echo "Unable to fetch container details"
}

# Check network connectivity
check_network() {
    print_header "Checking Network Connectivity"
    
    # Check if port 8080 is accessible
    if timeout 2 bash -c 'cat < /dev/null > /dev/tcp/localhost/8080' 2>/dev/null; then
        print_success "Port 8080 is accessible"
    else
        print_warning "Port 8080 is not accessible (application may still be starting)"
    fi
    
    # Check if port 55000 is accessible
    if timeout 2 bash -c 'cat < /dev/null > /dev/tcp/localhost/55000' 2>/dev/null; then
        print_success "Port 55000 (PostgreSQL) is accessible"
    else
        print_warning "Port 55000 (PostgreSQL) is not accessible"
    fi
    
    # Check if port 27017 is accessible
    if timeout 2 bash -c 'cat < /dev/null > /dev/tcp/localhost/27017' 2>/dev/null; then
        print_success "Port 27017 (MongoDB) is accessible"
    else
        print_warning "Port 27017 (MongoDB) is not accessible"
    fi
}

# Test application health endpoint
test_health_endpoint() {
    print_header "Testing Health Endpoint"
    
    response=$(timeout 10 curl -s -w "\n%{http_code}" http://localhost:8080/actuator/health 2>/dev/null)
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" = "200" ]; then
        print_success "Health endpoint returned 200 OK"
        if echo "$body" | grep -q '"status":"UP"'; then
            print_success "Application status is UP"
            if command_exists jq; then
                echo "$body" | jq . 2>/dev/null || echo "$body"
            else
                echo "$body"
            fi
        else
            print_error "Application status is not UP"
            echo "$body"
        fi
    else
        print_error "Health endpoint returned $http_code (application may still be starting)"
        echo "$body"
    fi
}

# Test GET all users endpoint
test_get_users() {
    print_header "Testing GET /api/users"
    
    response=$(curl -s -w "\n%{http_code}" http://localhost:8080/api/users)
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" = "200" ]; then
        print_success "GET /api/users returned 200 OK"
        user_count=$(echo "$body" | jq '. | length' 2>/dev/null || echo "0")
        print_info "Found $user_count user(s)"
        if command_exists jq; then
            echo "$body" | jq .
        else
            echo "$body"
        fi
    else
        print_error "GET /api/users returned $http_code"
        echo "$body"
    fi
}

# Test POST create user endpoint
test_create_user() {
    print_header "Testing POST /api/users (Create User)"
    
    timestamp=$(date +%s)
    test_email="test${timestamp}@example.com"
    
    response=$(curl -s -w "\n%{http_code}" -X POST http://localhost:8080/api/users \
        -H "Content-Type: application/json" \
        -d "{
            \"name\": \"Test User ${timestamp}\",
            \"email\": \"${test_email}\",
            \"phone\": \"1234567890\",
            \"address\": \"Test Address\"
        }")
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
        print_success "POST /api/users returned $http_code"
        user_id=$(echo "$body" | jq -r '.id' 2>/dev/null)
        print_info "Created user with ID: $user_id"
        if command_exists jq; then
            echo "$body" | jq .
        else
            echo "$body"
        fi
        echo "$user_id" > /tmp/test_user_id.txt
    else
        print_error "POST /api/users returned $http_code"
        echo "$body"
    fi
}

# Test GET user by ID endpoint
test_get_user_by_id() {
    print_header "Testing GET /api/users/{id}"
    
    if [ -f /tmp/test_user_id.txt ]; then
        user_id=$(cat /tmp/test_user_id.txt)
        
        response=$(curl -s -w "\n%{http_code}" http://localhost:8080/api/users/${user_id})
        http_code=$(echo "$response" | tail -n1)
        body=$(echo "$response" | sed '$d')
        
        if [ "$http_code" = "200" ]; then
            print_success "GET /api/users/${user_id} returned 200 OK"
            if command_exists jq; then
                echo "$body" | jq .
            else
                echo "$body"
            fi
        else
            print_error "GET /api/users/${user_id} returned $http_code"
            echo "$body"
        fi
    else
        print_warning "Skipping - no user ID available from previous test"
    fi
}

# Test PUT update user endpoint
test_update_user() {
    print_header "Testing PUT /api/users/{id} (Update User)"
    
    if [ -f /tmp/test_user_id.txt ]; then
        user_id=$(cat /tmp/test_user_id.txt)
        timestamp=$(date +%s)
        
        response=$(curl -s -w "\n%{http_code}" -X PUT http://localhost:8080/api/users/${user_id} \
            -H "Content-Type: application/json" \
            -d "{
                \"name\": \"Updated User ${timestamp}\",
                \"email\": \"updated${timestamp}@example.com\",
                \"phone\": \"9876543210\",
                \"address\": \"Updated Address\"
            }")
        
        http_code=$(echo "$response" | tail -n1)
        body=$(echo "$response" | sed '$d')
        
        if [ "$http_code" = "200" ]; then
            print_success "PUT /api/users/${user_id} returned 200 OK"
            if command_exists jq; then
                echo "$body" | jq .
            else
                echo "$body"
            fi
        else
            print_error "PUT /api/users/${user_id} returned $http_code"
            echo "$body"
        fi
    else
        print_warning "Skipping - no user ID available from previous test"
    fi
}

# Test audit logs endpoint
test_audit_logs() {
    print_header "Testing GET /api/audit (Audit Logs)"
    
    response=$(curl -s -w "\n%{http_code}" http://localhost:8080/api/audit)
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" = "200" ]; then
        print_success "GET /api/audit returned 200 OK"
        audit_count=$(echo "$body" | jq '. | length' 2>/dev/null || echo "0")
        print_info "Found $audit_count audit log(s)"
        if command_exists jq; then
            echo "$body" | jq '. | .[0:2]'  # Show first 2 audit logs
        else
            echo "$body"
        fi
    else
        print_error "GET /api/audit returned $http_code"
        echo "$body"
    fi
}

# Test audit logs by user ID
test_audit_by_user() {
    print_header "Testing GET /api/audit/user/{userId}"
    
    if [ -f /tmp/test_user_id.txt ]; then
        user_id=$(cat /tmp/test_user_id.txt)
        
        response=$(curl -s -w "\n%{http_code}" http://localhost:8080/api/audit/user/${user_id})
        http_code=$(echo "$response" | tail -n1)
        body=$(echo "$response" | sed '$d')
        
        if [ "$http_code" = "200" ]; then
            print_success "GET /api/audit/user/${user_id} returned 200 OK"
            audit_count=$(echo "$body" | jq '. | length' 2>/dev/null || echo "0")
            print_info "Found $audit_count audit log(s) for user $user_id"
            if command_exists jq; then
                echo "$body" | jq .
            else
                echo "$body"
            fi
        else
            print_error "GET /api/audit/user/${user_id} returned $http_code"
            echo "$body"
        fi
    else
        print_warning "Skipping - no user ID available from previous test"
    fi
}

# Test DELETE user endpoint
test_delete_user() {
    print_header "Testing DELETE /api/users/{id}"
    
    if [ -f /tmp/test_user_id.txt ]; then
        user_id=$(cat /tmp/test_user_id.txt)
        
        response=$(curl -s -w "\n%{http_code}" -X DELETE http://localhost:8080/api/users/${user_id})
        http_code=$(echo "$response" | tail -n1)
        
        if [ "$http_code" = "204" ] || [ "$http_code" = "200" ]; then
            print_success "DELETE /api/users/${user_id} returned $http_code"
        else
            print_error "DELETE /api/users/${user_id} returned $http_code"
        fi
        
        rm -f /tmp/test_user_id.txt
    else
        print_warning "Skipping - no user ID available from previous test"
    fi
}

# Check database connectivity
test_database_connectivity() {
    print_header "Testing Database Connectivity"
    
    # Test PostgreSQL
    print_info "Testing PostgreSQL connection..."
    if docker-compose exec -T postgres psql -U admin -d myapp -c "SELECT 1;" > /dev/null 2>&1; then
        print_success "PostgreSQL connection successful"
        
        # Count users in database
        user_count=$(docker-compose exec -T postgres psql -U admin -d myapp -t -c "SELECT COUNT(*) FROM users;" 2>/dev/null | tr -d ' \n')
        print_info "Users in PostgreSQL: $user_count"
    else
        print_error "PostgreSQL connection failed"
    fi
    
    # Test MongoDB
    print_info "Testing MongoDB connection..."
    if docker-compose exec -T mongodb mongosh -u admin -p admin123 --authenticationDatabase admin --quiet --eval "db.adminCommand('ping')" > /dev/null 2>&1; then
        print_success "MongoDB connection successful"
        
        # Count audit logs in database
        audit_count=$(docker-compose exec -T mongodb mongosh -u admin -p admin123 --authenticationDatabase admin audit_db --quiet --eval "db.user_audit_logs.countDocuments()" 2>/dev/null | tail -n1)
        print_info "Audit logs in MongoDB: $audit_count"
    else
        print_error "MongoDB connection failed"
    fi
}

# View container logs
view_logs() {
    print_header "Recent Container Logs"
    
    print_info "Spring Boot Application Logs (last 20 lines):"
    docker-compose logs --tail=20 app
    
    echo ""
    print_info "PostgreSQL Logs (last 10 lines):"
    docker-compose logs --tail=10 postgres
    
    echo ""
    print_info "MongoDB Logs (last 10 lines):"
    docker-compose logs --tail=10 mongodb
}

# Display summary
display_summary() {
    print_header "Test Summary"
    
    total=$((PASSED + FAILED))
    
    echo -e "${GREEN}Passed: $PASSED${NC}"
    echo -e "${RED}Failed: $FAILED${NC}"
    echo -e "Total:  $total"
    
    if [ $FAILED -eq 0 ]; then
        echo ""
        echo -e "${GREEN}🎉 All tests passed! Your Docker setup is working perfectly!${NC}"
        echo ""
        echo -e "${CYAN}Application URLs:${NC}"
        echo -e "  • Application: ${BLUE}http://localhost:8080${NC}"
        echo -e "  • Health Check: ${BLUE}http://localhost:8080/actuator/health${NC}"
        echo -e "  • Users API: ${BLUE}http://localhost:8080/api/users${NC}"
        echo -e "  • Audit API: ${BLUE}http://localhost:8080/api/audit${NC}"
    else
        echo ""
        echo -e "${RED}⚠ Some tests failed. Please check the logs above.${NC}"
        echo ""
        echo -e "${YELLOW}Troubleshooting:${NC}"
        echo -e "  1. Check logs: ${BLUE}docker-compose logs -f${NC}"
        echo -e "  2. Restart services: ${BLUE}docker-compose restart${NC}"
        echo -e "  3. Check status: ${BLUE}docker-compose ps${NC}"
    fi
}

# Main execution
main() {
    clear
    echo -e "${CYAN}"
    echo "╔════════════════════════════════════════════════════════════╗"
    echo "║   Docker Container Verification & Testing Script          ║"
    echo "║   Spring Boot + PostgreSQL + MongoDB                       ║"
    echo "╚════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
    
    check_prerequisites
    check_docker_daemon
    check_containers
    check_network
    test_health_endpoint
    test_get_users
    test_create_user
    test_get_user_by_id
    test_update_user
    test_audit_logs
    test_audit_by_user
    test_delete_user
    test_database_connectivity
    
    # Uncomment to view logs
    # view_logs
    
    display_summary
}

# Run main function
main
