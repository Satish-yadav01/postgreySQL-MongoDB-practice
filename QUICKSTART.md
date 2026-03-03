# Quick Start Guide

## Prerequisites Check
Before starting, ensure you have:
- [ ] Java 17 or higher installed (`java -version`)
- [ ] Maven installed (`mvn -version`)
- [ ] PostgreSQL installed and running (`psql --version`)

## Step-by-Step Setup

### 1. Database Setup

**Option A: Using psql command line**
```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE crud_db;

# Exit psql
\q
```

**Option B: Using the provided SQL script**
```bash
psql -U postgres -f database-setup.sql
```

### 2. Configure Database Credentials

Edit `src/main/resources/application.properties` and update:
```properties
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
```

### 3. Build the Project

```bash
# Clean and build
./mvnw clean install

# Or on Windows
mvnw.cmd clean install
```

### 4. Run the Application

```bash
# Run with Maven
./mvnw spring-boot:run

# Or run the JAR directly
java -jar target/practice-postgreySQL-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

### 5. Verify the Application

**Check if the application is running:**
```bash
curl http://localhost:8080/api/users
```

Expected response: `[]` (empty array if no users exist)

### 6. Test the API

**Create your first user:**
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

**Get all users:**
```bash
curl http://localhost:8080/api/users
```

## Using Postman

1. Import the `postman-collection.json` file into Postman
2. All API endpoints will be available in the collection
3. Update the base URL if needed (default: `http://localhost:8080`)

## Troubleshooting

### Issue: Connection refused to PostgreSQL
**Solution:** Ensure PostgreSQL is running
```bash
# On Linux
sudo systemctl status postgresql
sudo systemctl start postgresql

# On Mac
brew services start postgresql

# On Windows
# Check Services app for PostgreSQL service
```

### Issue: Database "crud_db" does not exist
**Solution:** Create the database manually
```bash
psql -U postgres -c "CREATE DATABASE crud_db;"
```

### Issue: Port 8080 already in use
**Solution:** Change the port in `application.properties`
```properties
server.port=8081
```

### Issue: Authentication failed for user
**Solution:** Update credentials in `application.properties` with correct PostgreSQL username/password

## Development Tips

1. **Enable development profile:**
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

2. **View SQL queries:**
   Check the console logs - SQL queries are logged with formatting

3. **Database changes:**
   The application uses `spring.jpa.hibernate.ddl-auto=update`
   - Tables are created automatically
   - Schema changes are applied automatically
   - For production, use `validate` or `none`

4. **Hot reload:**
   Add Spring Boot DevTools for automatic restart on code changes

## Next Steps

- Explore all API endpoints in the README.md
- Check the code structure and design patterns used
- Customize the User entity for your needs
- Add more entities and relationships
- Implement pagination and sorting
- Add security with Spring Security
- Write unit and integration tests

## Support

For issues or questions:
1. Check the main README.md for detailed documentation
2. Review the code comments and Javadocs
3. Check application logs for error details
