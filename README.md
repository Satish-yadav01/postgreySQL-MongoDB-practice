# PostgreSQL CRUD Application

A production-ready Spring Boot CRUD application with PostgreSQL database integration.

## Features

- ✅ Complete CRUD operations (Create, Read, Update, Delete)
- ✅ RESTful API design
- ✅ PostgreSQL database integration
- ✅ Input validation
- ✅ Global exception handling
- ✅ DTO pattern for data transfer
- ✅ Service layer pattern
- ✅ Repository pattern with JPA
- ✅ Mapper pattern for entity-DTO conversion
- ✅ Transaction management
- ✅ Logging with SLF4J
- ✅ Lombok for boilerplate reduction

## Technology Stack

- **Java 17**
- **Spring Boot 4.0.3**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Maven**

## Prerequisites

1. Java 17 or higher
2. Maven 3.6+
3. PostgreSQL 12 or higher

## Database Setup

1. Install PostgreSQL if not already installed
2. Create a database:
```sql
CREATE DATABASE crud_db;
```

3. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crud_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Create User
```http
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main St",
  "active": true
}
```

### Get All Users
```http
GET /api/users
```

### Get User by ID
```http
GET /api/users/{id}
```

### Get User by Email
```http
GET /api/users/email/{email}
```

### Get Active Users
```http
GET /api/users/active
```

### Search Users by Name
```http
GET /api/users/search?name=John
```

### Update User
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "phone": "9876543210",
  "address": "456 Oak Ave",
  "active": true
}
```

### Delete User
```http
DELETE /api/users/{id}
```

## Response Codes

- `200 OK` - Successful GET/PUT request
- `201 Created` - Successful POST request
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Validation error
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource (e.g., email already exists)
- `500 Internal Server Error` - Server error

## Error Response Format

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: '1'",
  "path": "/api/users/1"
}
```

## Validation Rules

- **name**: Required, 2-100 characters
- **email**: Required, valid email format, unique, max 100 characters
- **phone**: Optional, max 15 characters
- **address**: Optional, max 200 characters
- **active**: Optional, defaults to true

## Design Patterns Used

1. **Layered Architecture**: Controller → Service → Repository
2. **DTO Pattern**: Separate request/response objects from entities
3. **Mapper Pattern**: Convert between entities and DTOs
4. **Repository Pattern**: Data access abstraction with Spring Data JPA
5. **Dependency Injection**: Constructor-based injection with Lombok's @RequiredArgsConstructor
6. **Builder Pattern**: Using Lombok's @Builder for object creation
7. **Exception Handling**: Global exception handler with @RestControllerAdvice

## Project Structure

```
src/main/java/com/satish/practicepostgreysql/
├── controller/          # REST Controllers
│   └── UserController.java
├── service/            # Business logic layer
│   ├── UserService.java
│   └── UserServiceImpl.java
├── repository/         # Data access layer
│   └── UserRepository.java
├── entity/            # JPA entities
│   └── User.java
├── dto/               # Data Transfer Objects
│   ├── UserRequestDTO.java
│   └── UserResponseDTO.java
├── mapper/            # Entity-DTO mappers
│   └── UserMapper.java
├── exception/         # Custom exceptions and handlers
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
└── PracticePostgreySqlApplication.java
```

## Testing with cURL

### Create a user:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "address": "123 Main St"
  }'
```

### Get all users:
```bash
curl http://localhost:8080/api/users
```

### Get user by ID:
```bash
curl http://localhost:8080/api/users/1
```

### Update user:
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "phone": "9876543210",
    "address": "456 Oak Ave"
  }'
```

### Delete user:
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Best Practices Implemented

1. **Separation of Concerns**: Clear separation between layers
2. **Input Validation**: Bean validation with custom messages
3. **Exception Handling**: Centralized error handling
4. **Transaction Management**: @Transactional annotations
5. **Logging**: Comprehensive logging at each layer
6. **Immutability**: Using DTOs to prevent entity exposure
7. **Database Indexing**: Index on email for faster queries
8. **Timestamps**: Automatic creation and update timestamps
9. **RESTful Design**: Following REST principles
10. **Code Documentation**: Javadoc comments for all public methods

## License

This project is open source and available under the MIT License.
