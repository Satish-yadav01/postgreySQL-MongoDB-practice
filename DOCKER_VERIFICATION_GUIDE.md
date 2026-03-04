# ✅ Docker Verification & Testing Guide

## 🔍 Step-by-Step Verification Commands

### 1. Check Container Status
```bash
docker-compose ps
```

**Expected Output:**
```
NAME              STATUS
postgres-db       Up (healthy)
mongodb           Up (healthy)
spring-boot-app   Up (healthy)
```

✅ **All containers should show "Up" and "(healthy)"**

---

### 2. Check Application Health
```bash
curl http://localhost:8080/actuator/health
```

**Expected Output:**
```json
{
  "status": "UP"
}
```

✅ **Status should be "UP"**

---

### 3. Test GET All Users
```bash
curl http://localhost:8080/api/users
```

**Expected Output:**
```json
[]
```
or a list of users if any exist.

✅ **Should return 200 OK with JSON array**

---

### 4. Test CREATE User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "phone": "1234567890",
    "address": "Test Address"
  }'
```

**Expected Output:**
```json
{
  "id": 1,
  "name": "Test User",
  "email": "test@example.com",
  "phone": "1234567890",
  "address": "Test Address",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

✅ **Should return 200/201 with created user data**

---

### 5. Test GET User by ID
```bash
curl http://localhost:8080/api/users/1
```

**Expected Output:**
```json
{
  "id": 1,
  "name": "Test User",
  ...
}
```

✅ **Should return the user with ID 1**

---

### 6. Test UPDATE User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated User",
    "email": "updated@example.com",
    "phone": "9876543210",
    "address": "Updated Address"
  }'
```

**Expected Output:**
```json
{
  "id": 1,
  "name": "Updated User",
  "email": "updated@example.com",
  ...
}
```

✅ **Should return 200 with updated user data**

---

### 7. Test Audit Logs
```bash
curl http://localhost:8080/api/audit
```

**Expected Output:**
```json
[
  {
    "id": "...",
    "userId": 1,
    "operation": "CREATE",
    "status": "SUCCESS",
    ...
  }
]
```

✅ **Should return audit logs from MongoDB**

---

### 8. Test Audit Logs by User ID
```bash
curl http://localhost:8080/api/audit/user/1
```

**Expected Output:**
```json
[
  {
    "userId": 1,
    "operation": "CREATE",
    ...
  },
  {
    "userId": 1,
    "operation": "UPDATE",
    ...
  }
]
```

✅ **Should return audit logs for user ID 1**

---

### 9. Test DELETE User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

**Expected Output:**
```
(No content - 204 status)
```

✅ **Should return 204 No Content**

---

## 🗄️ Database Verification

### Check PostgreSQL
```bash
# Connect to PostgreSQL
docker-compose exec postgres psql -U admin -d myapp

# Inside psql, run:
\dt                    # List tables
SELECT * FROM users;   # View users
\q                     # Exit
```

### Check MongoDB
```bash
# Connect to MongoDB
docker-compose exec mongodb mongosh -u admin -p admin123 --authenticationDatabase admin

# Inside mongosh, run:
use audit_db                              # Switch to audit_db
db.user_audit_logs.find().pretty()       # View audit logs
db.user_audit_logs.countDocuments()      # Count audit logs
exit                                      # Exit
```

---

## 📊 Container Logs

### View Application Logs
```bash
# All logs
docker-compose logs app

# Follow logs (real-time)
docker-compose logs -f app

# Last 50 lines
docker-compose logs --tail=50 app

# With timestamps
docker-compose logs -f -t app
```

### View Database Logs
```bash
# PostgreSQL logs
docker-compose logs postgres

# MongoDB logs
docker-compose logs mongodb

# All services
docker-compose logs -f
```

---

## 🔧 Container Management

### Check Resource Usage
```bash
docker stats
```

### Restart Containers
```bash
# Restart all
docker-compose restart

# Restart specific service
docker-compose restart app
docker-compose restart postgres
docker-compose restart mongodb
```

### Stop Containers
```bash
# Stop all
docker-compose stop

# Stop specific service
docker-compose stop app
```

### Start Containers
```bash
# Start all
docker-compose start

# Start specific service
docker-compose start app
```

---

## 🧪 Complete Test Sequence

Run these commands in order to fully test your setup:

```bash
# 1. Check status
docker-compose ps

# 2. Health check
curl http://localhost:8080/actuator/health

# 3. Get all users (should be empty initially)
curl http://localhost:8080/api/users

# 4. Create user 1
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com","phone":"1111111111"}'

# 5. Create user 2
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob","email":"bob@example.com","phone":"2222222222"}'

# 6. Get all users (should show 2 users)
curl http://localhost:8080/api/users

# 7. Get user by ID
curl http://localhost:8080/api/users/1

# 8. Update user
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Updated","email":"alice.updated@example.com","phone":"9999999999"}'

# 9. Get audit logs
curl http://localhost:8080/api/audit

# 10. Get audit logs for user 1
curl http://localhost:8080/api/audit/user/1

# 11. Search users by name
curl "http://localhost:8080/api/users/search?name=Alice"

# 12. Get active users
curl http://localhost:8080/api/users/active

# 13. Delete user
curl -X DELETE http://localhost:8080/api/users/2

# 14. Verify deletion
curl http://localhost:8080/api/users
```

---

## 🎯 Quick Verification Checklist

Run this one-liner to check everything:

```bash
echo "=== Container Status ===" && \
docker-compose ps && \
echo -e "\n=== Health Check ===" && \
curl -s http://localhost:8080/actuator/health && \
echo -e "\n\n=== Users API ===" && \
curl -s http://localhost:8080/api/users && \
echo -e "\n\n=== Audit API ===" && \
curl -s http://localhost:8080/api/audit | head -c 200 && \
echo -e "\n\n✅ All checks complete!"
```

---

## 📈 Performance Testing

### Test Response Time
```bash
# Test health endpoint
time curl http://localhost:8080/actuator/health

# Test users endpoint
time curl http://localhost:8080/api/users

# Multiple requests
for i in {1..10}; do
  time curl -s http://localhost:8080/api/users > /dev/null
done
```

### Load Testing (if Apache Bench is installed)
```bash
# 100 requests, 10 concurrent
ab -n 100 -c 10 http://localhost:8080/api/users

# POST requests
ab -n 50 -c 5 -p user.json -T application/json http://localhost:8080/api/users
```

---

## 🐛 Troubleshooting Commands

### Container Not Starting
```bash
# Check logs for errors
docker-compose logs app

# Check if port is in use
sudo lsof -i :8080
sudo lsof -i :55000
sudo lsof -i :27017

# Restart with fresh build
docker-compose down
docker-compose up -d --build
```

### Database Connection Issues
```bash
# Test PostgreSQL connection
docker-compose exec postgres pg_isready -U admin

# Test MongoDB connection
docker-compose exec mongodb mongosh --eval "db.adminCommand('ping')"

# Check network
docker network ls
docker network inspect practice-postgreysql_app-network
```

### Application Errors
```bash
# View detailed logs
docker-compose logs -f app

# Check application inside container
docker-compose exec app sh
# Inside container:
ps aux
netstat -tulpn
exit
```

### Clean Restart
```bash
# Stop everything
docker-compose down

# Remove volumes (⚠️ deletes data)
docker-compose down -v

# Start fresh
docker-compose up -d

# Wait for health checks
sleep 30

# Verify
docker-compose ps
```

---

## 📊 Monitoring Commands

### Real-time Container Stats
```bash
# All containers
docker stats

# Specific container
docker stats spring-boot-app
```

### Disk Usage
```bash
# Docker disk usage
docker system df

# Volume usage
docker volume ls
docker volume inspect practice-postgreysql_postgres-data
```

### Network Inspection
```bash
# List networks
docker network ls

# Inspect network
docker network inspect practice-postgreysql_app-network
```

---

## ✅ Success Indicators

Your Docker setup is working correctly if:

1. ✅ All containers show "Up (healthy)" status
2. ✅ Health endpoint returns `{"status":"UP"}`
3. ✅ You can create, read, update, and delete users
4. ✅ Audit logs are being created in MongoDB
5. ✅ No errors in container logs
6. ✅ Databases are accessible and storing data
7. ✅ All API endpoints respond correctly

---

## 🎉 Your Setup is Working!

If all the above tests pass, congratulations! Your Docker setup is fully functional with:

- ✅ Spring Boot application running
- ✅ PostgreSQL storing user data
- ✅ MongoDB storing audit logs
- ✅ All services healthy and communicating
- ✅ Data persistence enabled
- ✅ API endpoints working correctly

---

## 📚 Additional Resources

- **Quick Commands**: See `DOCKER_CHEATSHEET.md`
- **Full Guide**: See `DOCKER_DEPLOYMENT_GUIDE.md`
- **Management Script**: Use `./docker-manage.sh`
- **Application Docs**: See `README.md`

---

**Need help? Run:** `docker-compose logs -f`
