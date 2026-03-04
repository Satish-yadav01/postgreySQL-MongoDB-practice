# 🐳 Quick Docker Setup

Get your Spring Boot application running in Docker containers in 3 simple steps!

---

## 🚀 Quick Start (3 Steps)

### Step 1: Start All Services
```bash
docker-compose up -d
```

### Step 2: Wait for Services (30-60 seconds)
```bash
docker-compose ps
```

### Step 3: Test the Application
```bash
curl http://localhost:8080/api/users
```

**That's it! Your application is running! 🎉**

---

## 📦 What Gets Started?

| Service | Port | Description |
|---------|------|-------------|
| **Spring Boot App** | 8080 | Your application |
| **PostgreSQL** | 55000 | User data database |
| **MongoDB** | 27017 | Audit logs database |

---

## 🛠️ Using the Management Script

We've included a handy script for common operations:

```bash
# Start everything
./docker-manage.sh start

# View logs
./docker-manage.sh logs

# Check status
./docker-manage.sh status

# Restart
./docker-manage.sh restart

# Stop everything
./docker-manage.sh stop

# Rebuild after code changes
./docker-manage.sh rebuild

# Test endpoints
./docker-manage.sh test

# Backup databases
./docker-manage.sh backup

# Clean up everything
./docker-manage.sh clean
```

---

## 📝 Common Commands

### View Logs
```bash
# All services
docker-compose logs -f

# Just the app
docker-compose logs -f app

# Last 50 lines
docker-compose logs --tail=50 app
```

### Check Status
```bash
docker-compose ps
```

### Restart After Code Changes
```bash
docker-compose up -d --build
```

### Stop Everything
```bash
docker-compose down
```

### Stop and Remove Data
```bash
docker-compose down -v
```

---

## 🧪 Test Your Setup

### 1. Health Check
```bash
curl http://localhost:8080/actuator/health
```
Expected: `{"status":"UP"}`

### 2. Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "phone": "1234567890"
  }'
```

### 3. Get All Users
```bash
curl http://localhost:8080/api/users
```

### 4. Check Audit Logs
```bash
curl http://localhost:8080/api/audit
```

---

## 🐛 Troubleshooting

### Application Not Starting?

**Check logs:**
```bash
docker-compose logs app
```

**Restart everything:**
```bash
docker-compose restart
```

### Port Already in Use?

Edit `docker-compose.yml` and change the port:
```yaml
app:
  ports:
    - "8081:8080"  # Change 8080 to 8081
```

### Database Connection Issues?

**Check database health:**
```bash
docker-compose ps
```

All services should show `(healthy)` status.

**Restart databases:**
```bash
docker-compose restart postgres mongodb
```

---

## 📊 Accessing Databases

### PostgreSQL
```bash
docker-compose exec postgres psql -U admin -d myapp
```

### MongoDB
```bash
docker-compose exec mongodb mongosh -u admin -p admin123 --authenticationDatabase admin
```

---

## 🔄 After Making Code Changes

```bash
# Option 1: Using management script
./docker-manage.sh rebuild

# Option 2: Using docker-compose
docker-compose up -d --build
```

---

## 🧹 Clean Up

### Stop Services (Keep Data)
```bash
docker-compose stop
```

### Remove Containers (Keep Data)
```bash
docker-compose down
```

### Remove Everything (Including Data)
```bash
docker-compose down -v
```

---

## 📚 More Information

For detailed documentation, see:
- **[DOCKER_DEPLOYMENT_GUIDE.md](DOCKER_DEPLOYMENT_GUIDE.md)** - Complete Docker guide
- **[README.md](README.md)** - Application documentation
- **[QUICKSTART.md](QUICKSTART.md)** - Quick setup guide

---

## ✅ Verification Checklist

- [ ] Docker and Docker Compose installed
- [ ] Ports 8080, 27017, 55000 available
- [ ] Run `docker-compose up -d`
- [ ] Wait 30-60 seconds
- [ ] Check `docker-compose ps` - all services healthy
- [ ] Test `curl http://localhost:8080/api/users`
- [ ] Application working! 🎉

---

## 🎯 Key Files

| File | Purpose |
|------|---------|
| `Dockerfile` | Builds the Spring Boot application image |
| `docker-compose.yml` | Orchestrates all services |
| `.dockerignore` | Excludes files from Docker build |
| `docker-manage.sh` | Management script for common tasks |
| `application-prod.properties` | Production configuration |

---

## 💡 Tips

1. **First time setup takes longer** - Docker needs to download images
2. **Wait for health checks** - Services need time to start
3. **Check logs if issues** - `docker-compose logs -f`
4. **Use management script** - Easier than remembering commands
5. **Data persists** - Your data is safe in Docker volumes

---

## 🆘 Need Help?

```bash
# Check what's running
docker-compose ps

# View all logs
docker-compose logs -f

# Restart everything
docker-compose restart

# Start fresh
docker-compose down && docker-compose up -d
```

---

**Happy Dockerizing! 🐳**
