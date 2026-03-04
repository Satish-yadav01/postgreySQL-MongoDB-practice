#!/bin/bash

# Docker Management Script for Spring Boot Application
# This script provides easy commands to manage the Docker containers

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker is not running. Please start Docker first."
        exit 1
    fi
}

# Function to build and start all services
start() {
    print_info "Starting all services..."
    docker-compose up -d
    print_info "Services started successfully!"
    print_info "Application will be available at http://localhost:8080"
    print_info "Waiting for services to be healthy..."
    sleep 10
    docker-compose ps
}

# Function to stop all services
stop() {
    print_info "Stopping all services..."
    docker-compose stop
    print_info "Services stopped successfully!"
}

# Function to restart all services
restart() {
    print_info "Restarting all services..."
    docker-compose restart
    print_info "Services restarted successfully!"
}

# Function to view logs
logs() {
    if [ -z "$1" ]; then
        print_info "Showing logs for all services..."
        docker-compose logs -f
    else
        print_info "Showing logs for $1..."
        docker-compose logs -f "$1"
    fi
}

# Function to check status
status() {
    print_info "Checking status of all services..."
    docker-compose ps
}

# Function to rebuild and restart
rebuild() {
    print_info "Rebuilding application..."
    docker-compose build --no-cache app
    print_info "Restarting application..."
    docker-compose up -d app
    print_info "Application rebuilt and restarted successfully!"
}

# Function to clean up everything
clean() {
    print_warning "This will remove all containers, networks, and volumes!"
    read -p "Are you sure? (yes/no): " confirm
    if [ "$confirm" = "yes" ]; then
        print_info "Cleaning up..."
        docker-compose down -v --rmi local
        print_info "Cleanup completed!"
    else
        print_info "Cleanup cancelled."
    fi
}

# Function to test the application
test() {
    print_info "Testing application endpoints..."
    
    # Test health endpoint
    print_info "Testing health endpoint..."
    if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
        print_info "✓ Health check passed"
    else
        print_error "✗ Health check failed"
    fi
    
    # Test users endpoint
    print_info "Testing users endpoint..."
    if curl -s http://localhost:8080/api/users > /dev/null; then
        print_info "✓ Users endpoint accessible"
    else
        print_error "✗ Users endpoint not accessible"
    fi
    
    # Test audit endpoint
    print_info "Testing audit endpoint..."
    if curl -s http://localhost:8080/api/audit > /dev/null; then
        print_info "✓ Audit endpoint accessible"
    else
        print_error "✗ Audit endpoint not accessible"
    fi
}

# Function to backup databases
backup() {
    print_info "Creating database backups..."
    
    # Backup PostgreSQL
    print_info "Backing up PostgreSQL..."
    docker-compose exec -T postgres pg_dump -U admin myapp > "backup-postgres-$(date +%Y%m%d-%H%M%S).sql"
    print_info "PostgreSQL backup created"
    
    # Backup MongoDB
    print_info "Backing up MongoDB..."
    docker-compose exec -T mongodb mongodump -u admin -p admin123 --authenticationDatabase admin -d audit_db --archive > "backup-mongodb-$(date +%Y%m%d-%H%M%S).archive"
    print_info "MongoDB backup created"
    
    print_info "Backups completed successfully!"
}

# Function to show help
show_help() {
    echo "Docker Management Script for Spring Boot Application"
    echo ""
    echo "Usage: ./docker-manage.sh [command]"
    echo ""
    echo "Commands:"
    echo "  start       - Build and start all services"
    echo "  stop        - Stop all services"
    echo "  restart     - Restart all services"
    echo "  logs [svc]  - View logs (optionally for specific service: app, postgres, mongodb)"
    echo "  status      - Check status of all services"
    echo "  rebuild     - Rebuild and restart the application"
    echo "  clean       - Remove all containers, networks, and volumes"
    echo "  test        - Test application endpoints"
    echo "  backup      - Backup databases"
    echo "  help        - Show this help message"
    echo ""
    echo "Examples:"
    echo "  ./docker-manage.sh start"
    echo "  ./docker-manage.sh logs app"
    echo "  ./docker-manage.sh rebuild"
}

# Main script logic
check_docker

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    logs)
        logs "$2"
        ;;
    status)
        status
        ;;
    rebuild)
        rebuild
        ;;
    clean)
        clean
        ;;
    test)
        test
        ;;
    backup)
        backup
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        print_error "Unknown command: $1"
        echo ""
        show_help
        exit 1
        ;;
esac
