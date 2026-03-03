# API Testing Guide

This guide provides detailed examples for testing all API endpoints.

## Prerequisites

- Application running on `http://localhost:8080`
- PostgreSQL database `crud_db` created and accessible
- Tool for testing: cURL, Postman, or any REST client

---

## 1. Create User (POST)

### Request
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "address": "123 Main Street, New York, NY",
    "active": true
  }'
```

### Expected Response (201 Created)
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main Street, New York, NY",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Validation Error Example (400 Bad Request)
```bash
# Missing required field
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "invalid-email"
  }'
```

Response:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "name": "Name is required",
    "email": "Email should be valid"
  },
  "path": "/api/users"
}
```

---

## 2. Get All Users (GET)

### Request
```bash
curl http://localhost:8080/api/users
```

### Expected Response (200 OK)
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "address": "123 Main Street, New York, NY",
    "active": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "0987654321",
    "address": "456 Oak Avenue, Los Angeles, CA",
    "active": true,
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

---

## 3. Get User by ID (GET)

### Request
```bash
curl http://localhost:8080/api/users/1
```

### Expected Response (200 OK)
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main Street, New York, NY",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Not Found Error (404 Not Found)
```bash
curl http://localhost:8080/api/users/999
```

Response:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: '999'",
  "path": "/api/users/999"
}
```

---

## 4. Get User by Email (GET)

### Request
```bash
curl http://localhost:8080/api/users/email/john.doe@example.com
```

### Expected Response (200 OK)
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main Street, New York, NY",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

## 5. Get Active Users (GET)

### Request
```bash
curl http://localhost:8080/api/users/active
```

### Expected Response (200 OK)
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "address": "123 Main Street, New York, NY",
    "active": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

---

## 6. Search Users by Name (GET)

### Request
```bash
curl "http://localhost:8080/api/users/search?name=John"
```

### Expected Response (200 OK)
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "address": "123 Main Street, New York, NY",
    "active": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 3,
    "name": "Johnny Walker",
    "email": "johnny.walker@example.com",
    "phone": "5555555555",
    "address": "789 Pine Road, Chicago, IL",
    "active": true,
    "createdAt": "2024-01-15T12:00:00",
    "updatedAt": "2024-01-15T12:00:00"
  }
]
```

---

## 7. Update User (PUT)

### Request
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "phone": "9999999999",
    "address": "999 New Address, Boston, MA",
    "active": true
  }'
```

### Expected Response (200 OK)
```json
{
  "id": 1,
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "phone": "9999999999",
  "address": "999 New Address, Boston, MA",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:30:00"
}
```

### Duplicate Email Error (409 Conflict)
```bash
# Trying to update with an email that already exists
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "jane.smith@example.com",
    "phone": "1234567890",
    "address": "123 Main Street"
  }'
```

Response:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "User already exists with email: 'jane.smith@example.com'",
  "path": "/api/users/1"
}
```

---

## 8. Delete User (DELETE)

### Request
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### Expected Response (204 No Content)
No response body, just status code 204.

### Verify Deletion
```bash
curl http://localhost:8080/api/users/1
```

Response (404 Not Found):
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: '1'",
  "path": "/api/users/1"
}
```

---

## Complete Test Scenario

Here's a complete test scenario to verify all functionality:

```bash
# 1. Create first user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "phone": "1111111111",
    "address": "111 First St"
  }'

# 2. Create second user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Smith",
    "email": "bob@example.com",
    "phone": "2222222222",
    "address": "222 Second St"
  }'

# 3. Create third user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Charlie Brown",
    "email": "charlie@example.com",
    "phone": "3333333333",
    "address": "333 Third St"
  }'

# 4. Get all users
curl http://localhost:8080/api/users

# 5. Get user by ID
curl http://localhost:8080/api/users/1

# 6. Get user by email
curl http://localhost:8080/api/users/email/bob@example.com

# 7. Search users by name
curl "http://localhost:8080/api/users/search?name=Bob"

# 8. Update user
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Smith Updated",
    "email": "bob.updated@example.com",
    "phone": "9999999999",
    "address": "999 Updated St"
  }'

# 9. Get active users
curl http://localhost:8080/api/users/active

# 10. Delete user
curl -X DELETE http://localhost:8080/api/users/3

# 11. Verify deletion
curl http://localhost:8080/api/users/3

# 12. Get all users again
curl http://localhost:8080/api/users
```

---

## Testing with Postman

1. Import the `postman-collection.json` file
2. The collection includes all endpoints pre-configured
3. Run the entire collection or individual requests
4. Check the response status codes and bodies

---

## Common HTTP Status Codes

| Code | Status | Meaning |
|------|--------|---------|
| 200 | OK | Request successful (GET, PUT) |
| 201 | Created | Resource created successfully (POST) |
| 204 | No Content | Resource deleted successfully (DELETE) |
| 400 | Bad Request | Validation error or malformed request |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource (e.g., email exists) |
| 500 | Internal Server Error | Server error |

---

## Tips for Testing

1. **Start Fresh**: Delete all users and start with a clean database
2. **Test Validation**: Try invalid inputs to verify validation works
3. **Test Edge Cases**: Empty strings, null values, very long strings
4. **Test Duplicates**: Try creating users with duplicate emails
5. **Test Not Found**: Try accessing non-existent resources
6. **Check Timestamps**: Verify createdAt and updatedAt are set correctly
7. **Test Search**: Try partial name matches, case-insensitive search
8. **Monitor Logs**: Check application logs for SQL queries and errors

---

## Troubleshooting

### Issue: Connection Refused
- Ensure the application is running
- Check if port 8080 is available
- Verify the application started without errors

### Issue: Database Errors
- Ensure PostgreSQL is running
- Verify database `crud_db` exists
- Check database credentials in application.properties

### Issue: Validation Errors
- Ensure all required fields are provided
- Check field length constraints
- Verify email format is valid

---

## Next Steps

After testing the basic CRUD operations:
1. Test concurrent requests
2. Test with large datasets
3. Measure response times
4. Test error scenarios
5. Verify database state after operations
6. Test transaction rollback scenarios
