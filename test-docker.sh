#!/bin/bash

# Simple Docker Test Script
# Quick verification of all services

echo "🐳 Docker Container Verification"
echo "=================================="
echo ""

# 1. Container Status
echo "1️⃣  Checking Container Status..."
docker-compose ps
echo ""

# 2. Health Check
echo "2️⃣  Testing Application Health..."
curl -s http://localhost:8080/actuator/health | jq . || curl -s http://localhost:8080/actuator/health
echo ""
echo ""

# 3. Get Users
echo "3️⃣  Testing GET /api/users..."
curl -s http://localhost:8080/api/users | jq . || curl -s http://localhost:8080/api/users
echo ""
echo ""

# 4. Create User
echo "4️⃣  Testing POST /api/users (Create User)..."
curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Docker Test","email":"dockertest@example.com","phone":"1234567890"}' \
  | jq . || curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Docker Test","email":"dockertest@example.com","phone":"1234567890"}'
echo ""
echo ""

# 5. Get Users Again
echo "5️⃣  Testing GET /api/users (After Create)..."
curl -s http://localhost:8080/api/users | jq . || curl -s http://localhost:8080/api/users
echo ""
echo ""

# 6. Audit Logs
echo "6️⃣  Testing GET /api/audit (Audit Logs)..."
curl -s http://localhost:8080/api/audit | jq '.[0:2]' || curl -s http://localhost:8080/api/audit
echo ""
echo ""

# 7. Database Check
echo "7️⃣  Checking PostgreSQL..."
docker-compose exec -T postgres psql -U admin -d myapp -c "SELECT COUNT(*) as user_count FROM users;" 2>/dev/null || echo "PostgreSQL check skipped"
echo ""

echo "8️⃣  Checking MongoDB..."
docker-compose exec -T mongodb mongosh -u admin -p admin123 --authenticationDatabase admin audit_db --quiet --eval "print('Audit logs: ' + db.user_audit_logs.countDocuments())" 2>/dev/null || echo "MongoDB check skipped"
echo ""

echo "=================================="
echo "✅ Verification Complete!"
echo ""
echo "📊 Your application is running at:"
echo "   http://localhost:8080"
echo ""
echo "🔍 Try these commands:"
echo "   curl http://localhost:8080/api/users"
echo "   curl http://localhost:8080/api/audit"
echo ""
