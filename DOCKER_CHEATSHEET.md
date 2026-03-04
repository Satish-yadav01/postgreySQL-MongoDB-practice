# 🐳 Docker Quick Reference Card

## 🚀 Start Application (One Command)
```bash
docker-compose up -d
```

## 📊 Check Status
```bash
docker-compose ps
```

## 🔍 View Logs
```bash
docker-compose logs -f app
```

## 🧪 Test Application
```bash
curl http://localhost:8080/api/users
```

## 🔄 Restart
```bash
docker-compose restart
```

## 🛑 Stop
```bash
docker-compose down
```

## 🔨 Rebuild After Code Changes
```bash
docker-compose up -d --build
```

## 📦 Services & Ports
- **Application**: http://localhost:8080
- **PostgreSQL**: localhost:55000
- **MongoDB**: localhost:27017

## 🎯 Management Script
```bash
./docker-manage.sh start    # Start all
./docker-manage.sh logs     # View logs
./docker-manage.sh status   # Check status
./docker-manage.sh test     # Test endpoints
./docker-manage.sh stop     # Stop all
```

## 📚 Documentation
- **DOCKER_QUICKSTART.md** - Quick setup
- **DOCKER_DEPLOYMENT_GUIDE.md** - Full guide
- **DOCKER_IMPLEMENTATION_SUMMARY.md** - Overview

---
**That's all you need to know! 🎉**
