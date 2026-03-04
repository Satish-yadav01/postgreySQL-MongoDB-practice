# 🐳 Docker Deployment Guide

Complete guide to build and run the Spring Boot application with PostgreSQL and MongoDB using Docker.

---

## 📋 Prerequisites

- Docker installed (version 20.10+)
- Docker Compose installed (version 2.0+)
- At least 2GB of free RAM
- Ports 8080, 27017, and 55000 available

---

## 🏗️ Project Structure

```
practice-postgreySQL/
├── Dockerfile                  # Multi-stage Docker build
├── docker-compose.yml          # Orchestration file
├── .dockerignore              # Files to exclude from build
└── src/main/resources/
    ├── application.properties
    ├── application-dev.properties
    └── application-prod.properties  # Production config
```

---

## 🚀 Quick Start

### Option 1: Using Docker Compose (Recommended)

```bash
# 1. Navigate to project directory
cd /home/satish-yadav/Documents/satish/task/practice-postgreySQL

# 2. Build and start all services
docker-compose up -d

# 3. Check status
docker-compose ps

# 4. View logs
docker-compose logs -f app
```

### Option 2: Build Docker Image Only

```bash
# Build the image
docker build -t spring-boot-app:latest .

# Run with existing databases
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:55000/myapp \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin123 \
  -e SPRING_MONGODB_URI=mongodb://admin:admin123@host.docker.internal:27017/audit_db?authSource=admin \
  --name spring-boot-app \
  spring-boot-app:latest
```

---

## 📦 Docker Compose Services

### 1. PostgreSQL Database
- **Image**: postgres:16-alpine
- **Container Name**: postgres-db
- **Port**: 55000:5432
- **Database**: myapp
- **Credentials**: admin/admin123
- **Volume**: postgres-data (persistent storage)

### 2. MongoDB Database
- **Image**: mongo:7.0
- **Container Name**: mongodb
- **Port**: 27017:27017
- **Database**: audit_db
- **Credentials**: admin/admin123
- **Volumes**: mongodb-data, mongodb-config

### 3. Spring Boot Application
- **Build**: From Dockerfile
- **Container Name**: spring-boot-app
- **Port**: 8080:8080
- **Profile**: prod
- **Depends On**: postgres, mongodb (with health checks)

---

## 🔧 Configuration

### Environment Variables

The application uses these environment variables (configured in docker-compose.yml):

```yaml
# PostgreSQL
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/myapp
SPRING_DATASOURCE_USERNAME: admin
SPRING_DATASOURCE_PASSWORD: admin123

# MongoDB
SPRING_MONGODB_URI: mongodb://admin:admin123@mongodb:27017/audit_db?authSource=admin
SPRING_MONGODB_DATABASE: audit_db

# Application
SPRING_PROFILES_ACTIVE: prod
SERVER_PORT: 8080
```

### Custom Configuration

To override any configuration, edit `docker-compose.yml`:

```yaml
services:
  app:
    environment:
      - SPRING_JPA_SHOW_SQL=true
      - LOGGING_LEVEL_COM_SATISH_PRACTICEPOSTGREYSQL=DEBUG
```

---

## 📝 Docker Commands

### Build & Start

```bash
# Build and start all services
docker-compose up -d

# Build without cache
docker-compose build --no-cache

# Start specific service
docker-compose up -d postgres
```

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f app
docker-compose logs -f postgres
docker-compose logs -f mongodb

# Last 100 lines
docker-compose logs --tail=100 app
```

### Check Status

```bash
# List all containers
docker-compose ps

# Check health status
docker-compose ps app
```

### Stop & Remove

```bash
# Stop all services
docker-compose stop

# Stop and remove containers
docker-compose down

# Remove containers and volumes (⚠️ deletes data)
docker-compose down -v

# Remove containers, volumes, and images
docker-compose down -v --rmi all
```

### Restart Services

```bash
# Restart all services
docker-compose restart

# Restart specific service
docker-compose restart app
```

### Execute Commands

```bash
# Access Spring Boot container shell
docker-compose exec app sh

# Access PostgreSQL
docker-compose exec postgres psql -U admin -d myapp

# Access MongoDB
docker-compose exec mongodb mongosh -u admin -p admin123 --authenticationDatabase admin
```

---

## 🔍 Verification & Testing

### 1. Check Container Health

```bash
# Check all containers are running
docker-compose ps

# Expected output:
# NAME              STATUS                    PORTS
# postgres-db       Up (healthy)             0.0.0.0:55000->5432/tcp
# mongodb           Up (healthy)             0.0.0.0:27017->27017/tcp
# spring-boot-app   Up (healthy)             0.0.0.0:8080->8080/tcp
```

### 2. Check Application Health

```bash
# Health check endpoint
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP"}
```

### 3. Test API Endpoints

```bash
# Get all users
curl http://localhost:8080/api/users

# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "address": "123 Main St"
  }'

# Get user by ID
curl http://localhost:8080/api/users/1

# Get audit logs
curl http://localhost:8080/api/audit
```

### 4. Check Database Connections

**PostgreSQL:**
```bash
docker-compose exec postgres psql -U admin -d myapp -c "\dt"
```

**MongoDB:**
```bash
docker-compose exec mongodb mongosh -u admin -p admin123 --authenticationDatabase admin --eval "use audit_db; db.user_audit_logs.find().pretty()"
```

---

## 🐛 Troubleshooting

### Application Won't Start

```bash
# Check logs
docker-compose logs app

# Common issues:
# 1. Database not ready - wait for health checks
# 2. Port already in use - change ports in docker-compose.yml
# 3. Build failed - check Dockerfile and dependencies
```

### Database Connection Issues

```bash
# Check database health
docker-compose ps postgres
docker-compose ps mongodb

# Restart databases
docker-compose restart postgres mongodb

# Check network connectivity
docker-compose exec app ping postgres
docker-compose exec app ping mongodb
```

### Port Conflicts

If ports are already in use, edit `docker-compose.yml`:

```yaml
services:
  postgres:
    ports:
      - "55001:5432"  # Change 55000 to 55001
  
  mongodb:
    ports:
      - "27018:27017"  # Change 27017 to 27018
  
  app:
    ports:
      - "8081:8080"  # Change 8080 to 8081
```

### View Container Resource Usage

```bash
# Check CPU and memory usage
docker stats

# Check specific container
docker stats spring-boot-app
```

### Rebuild After Code Changes

```bash
# Rebuild and restart
docker-compose up -d --build

# Force rebuild without cache
docker-compose build --no-cache app
docker-compose up -d app
```

---

## 📊 Data Persistence

### Volumes

Data is persisted in Docker volumes:

```bash
# List volumes
docker volume ls

# Inspect volume
docker volume inspect practice-postgreysql_postgres-data
docker volume inspect practice-postgreysql_mongodb-data

# Backup PostgreSQL data
docker-compose exec postgres pg_dump -U admin myapp > backup.sql

# Backup MongoDB data
docker-compose exec mongodb mongodump -u admin -p admin123 --authenticationDatabase admin -d audit_db -o /tmp/backup
docker cp mongodb:/tmp/backup ./mongodb-backup
```

### Clean Up Volumes

```bash
# Remove all volumes (⚠️ deletes all data)
docker-compose down -v

# Remove specific volume
docker volume rm practice-postgreysql_postgres-data
```

---

## 🔐 Security Considerations

### Production Deployment

1. **Change Default Credentials**
   ```yaml
   environment:
     POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
     MONGO_INITDB_ROOT_PASSWORD: ${MONGO_PASSWORD}
   ```

2. **Use Environment Files**
   ```bash
   # Create .env file
   echo "POSTGRES_PASSWORD=secure_password" > .env
   echo "MONGO_PASSWORD=secure_password" >> .env
   ```

3. **Don't Expose Database Ports**
   ```yaml
   # Remove ports section for databases
   postgres:
     # ports:
     #   - "55000:5432"  # Comment out
   ```

4. **Use Docker Secrets** (for Docker Swarm)
   ```yaml
   secrets:
     postgres_password:
       external: true
   ```

---

## 🚀 Production Deployment

### Build for Production

```bash
# Build optimized image
docker build -t spring-boot-app:1.0.0 .

# Tag for registry
docker tag spring-boot-app:1.0.0 your-registry/spring-boot-app:1.0.0

# Push to registry
docker push your-registry/spring-boot-app:1.0.0
```

### Docker Swarm Deployment

```bash
# Initialize swarm
docker swarm init

# Deploy stack
docker stack deploy -c docker-compose.yml myapp

# Check services
docker service ls
```

### Kubernetes Deployment

```bash
# Generate Kubernetes manifests
kompose convert -f docker-compose.yml

# Apply to cluster
kubectl apply -f .
```

---

## 📈 Monitoring

### View Logs in Real-Time

```bash
# All services
docker-compose logs -f

# With timestamps
docker-compose logs -f -t

# Filter by service
docker-compose logs -f app | grep ERROR
```

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Database health
docker-compose exec postgres pg_isready -U admin
docker-compose exec mongodb mongosh --eval "db.adminCommand('ping')"
```

---

## 🎯 Performance Tuning

### JVM Options

Edit Dockerfile to add JVM tuning:

```dockerfile
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:InitialRAMPercentage=50.0", \
  "-XX:+UseG1GC", \
  "-jar", "app.jar"]
```

### Resource Limits

Add to docker-compose.yml:

```yaml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 1G
        reservations:
          cpus: '1'
          memory: 512M
```

---

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)

---

## ✅ Checklist

- [ ] Docker and Docker Compose installed
- [ ] Ports 8080, 27017, 55000 available
- [ ] Project built successfully
- [ ] All containers running and healthy
- [ ] Application accessible at http://localhost:8080
- [ ] API endpoints responding correctly
- [ ] Databases accessible and storing data
- [ ] Logs showing no errors

---

## 🎉 Success!

Your application is now running in Docker containers with:
- ✅ Spring Boot application
- ✅ PostgreSQL database
- ✅ MongoDB database
- ✅ Data persistence
- ✅ Health checks
- ✅ Network isolation
- ✅ Production-ready configuration

Access your application at: **http://localhost:8080**

---

**Need Help?**
- Check logs: `docker-compose logs -f`
- Restart services: `docker-compose restart`
- Clean start: `docker-compose down && docker-compose up -d`
