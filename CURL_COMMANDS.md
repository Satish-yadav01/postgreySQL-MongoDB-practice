# 🚀 Complete cURL Commands for All API Endpoints

This document contains ready-to-use cURL commands for all 8 endpoints in the UserController.

**Base URL:** `http://localhost:8080`

---

## 📋 Table of Contents
1. [Create User (POST)](#1-create-user-post)
2. [Get All Users (GET)](#2-get-all-users-get)
3. [Get User by ID (GET)](#3-get-user-by-id-get)
4. [Update User (PUT)](#4-update-user-put)
5. [Delete User (DELETE)](#5-delete-user-delete)
6. [Get User by Email (GET)](#6-get-user-by-email-get)
7. [Get Active Users (GET)](#7-get-active-users-get)
8. [Search Users by Name (GET)](#8-search-users-by-name-get)

---

## 1. Create User (POST)

**Endpoint:** `POST /api/users`  
**Status Code:** `201 Created`

### Basic Request
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

### With Pretty Print (using jq)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "address": "123 Main Street, New York, NY",
    "active": true
  }' | jq '.'
```

### Minimal Request (only required fields)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com"
  }'
```

### Multiple Users Creation
```bash
# User 1
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "phone": "1111111111",
    "address": "111 First Avenue"
  }'

# User 2
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Williams",
    "email": "bob@example.com",
    "phone": "2222222222",
    "address": "222 Second Street"
  }'

# User 3
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Charlie Brown",
    "email": "charlie@example.com",
    "phone": "3333333333",
    "address": "333 Third Road"
  }'
```

### Expected Response
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

## 2. Get All Users (GET)

**Endpoint:** `GET /api/users`  
**Status Code:** `200 OK`

### Basic Request
```bash
curl http://localhost:8080/api/users
```

### With Pretty Print
```bash
curl http://localhost:8080/api/users | jq '.'
```

### With Verbose Output
```bash
curl -v http://localhost:8080/api/users
```

### Save Response to File
```bash
curl http://localhost:8080/api/users -o users.json
```

### Expected Response
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
    "address": "456 Oak Avenue",
    "active": true,
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

---

## 3. Get User by ID (GET)

**Endpoint:** `GET /api/users/{id}`  
**Status Code:** `200 OK`

### Get User with ID 1
```bash
curl http://localhost:8080/api/users/1
```

### Get User with ID 2
```bash
curl http://localhost:8080/api/users/2
```

### Get User with ID 5
```bash
curl http://localhost:8080/api/users/5
```

### With Pretty Print
```bash
curl http://localhost:8080/api/users/1 | jq '.'
```

### With Headers Display
```bash
curl -i http://localhost:8080/api/users/1
```

### Expected Response (Success)
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

### Expected Response (Not Found - 404)
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

## 4. Update User (PUT)

**Endpoint:** `PUT /api/users/{id}`  
**Status Code:** `200 OK`

### Update User with ID 1
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "phone": "9999999999",
    "address": "999 Updated Street, Boston, MA",
    "active": true
  }'
```

### Update Only Name and Email
```bash
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith Updated",
    "email": "jane.updated@example.com",
    "phone": "0987654321",
    "address": "456 Oak Avenue"
  }'
```

### Deactivate User
```bash
curl -X PUT http://localhost:8080/api/users/3 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Charlie Brown",
    "email": "charlie@example.com",
    "phone": "3333333333",
    "address": "333 Third Road",
    "active": false
  }'
```

### With Pretty Print
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "phone": "9999999999",
    "address": "999 Updated Street",
    "active": true
  }' | jq '.'
```

### Expected Response
```json
{
  "id": 1,
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "phone": "9999999999",
  "address": "999 Updated Street, Boston, MA",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:30:00"
}
```

---

## 5. Delete User (DELETE)

**Endpoint:** `DELETE /api/users/{id}`  
**Status Code:** `204 No Content`

### Delete User with ID 1
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### Delete User with ID 5
```bash
curl -X DELETE http://localhost:8080/api/users/5
```

### With Verbose Output
```bash
curl -v -X DELETE http://localhost:8080/api/users/1
```

### With Status Code Display
```bash
curl -w "\nHTTP Status: %{http_code}\n" -X DELETE http://localhost:8080/api/users/1
```

### Expected Response
```
(No response body - Status Code: 204 No Content)
```

### Verify Deletion
```bash
# Try to get the deleted user - should return 404
curl http://localhost:8080/api/users/1
```

---

## 6. Get User by Email (GET)

**Endpoint:** `GET /api/users/email/{email}`  
**Status Code:** `200 OK`

### Get User by Email
```bash
curl http://localhost:8080/api/users/email/john.doe@example.com
```

### Another Example
```bash
curl http://localhost:8080/api/users/email/jane.smith@example.com
```

### With Pretty Print
```bash
curl http://localhost:8080/api/users/email/john.doe@example.com | jq '.'
```

### URL Encoded Email (if email has special characters)
```bash
curl http://localhost:8080/api/users/email/user%2Btest@example.com
```

### Expected Response
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

## 7. Get Active Users (GET)

**Endpoint:** `GET /api/users/active`  
**Status Code:** `200 OK`

### Get All Active Users
```bash
curl http://localhost:8080/api/users/active
```

### With Pretty Print
```bash
curl http://localhost:8080/api/users/active | jq '.'
```

### Count Active Users (using jq)
```bash
curl http://localhost:8080/api/users/active | jq 'length'
```

### Get Only Names of Active Users
```bash
curl http://localhost:8080/api/users/active | jq '.[].name'
```

### Expected Response
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
    "address": "456 Oak Avenue",
    "active": true,
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

---

## 8. Search Users by Name (GET)

**Endpoint:** `GET /api/users/search?name={name}`  
**Status Code:** `200 OK`

### Search for "John"
```bash
curl "http://localhost:8080/api/users/search?name=John"
```

### Search for "Smith"
```bash
curl "http://localhost:8080/api/users/search?name=Smith"
```

### Search for "Bob"
```bash
curl "http://localhost:8080/api/users/search?name=Bob"
```

### With Pretty Print
```bash
curl "http://localhost:8080/api/users/search?name=John" | jq '.'
```

### Case-Insensitive Search
```bash
# These should return the same results
curl "http://localhost:8080/api/users/search?name=john"
curl "http://localhost:8080/api/users/search?name=JOHN"
curl "http://localhost:8080/api/users/search?name=John"
```

### Partial Name Search
```bash
# Search for partial name
curl "http://localhost:8080/api/users/search?name=Jo"
```

### URL Encoded Search (for names with spaces)
```bash
curl "http://localhost:8080/api/users/search?name=John%20Doe"
```

### Expected Response
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
    "id": 5,
    "name": "Johnny Walker",
    "email": "johnny.walker@example.com",
    "phone": "5555555555",
    "address": "555 Fifth Avenue",
    "active": true,
    "createdAt": "2024-01-15T12:00:00",
    "updatedAt": "2024-01-15T12:00:00"
  }
]
```

---

## 🔄 Complete Test Workflow

Here's a complete workflow to test all endpoints in sequence:

```bash
#!/bin/bash

echo "=== 1. Create Users ==="
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice Johnson", "email": "alice@example.com", "phone": "1111111111", "address": "111 First St"}'

curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Bob Smith", "email": "bob@example.com", "phone": "2222222222", "address": "222 Second St"}'

curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Charlie Brown", "email": "charlie@example.com", "phone": "3333333333", "address": "333 Third St"}'

echo -e "\n\n=== 2. Get All Users ==="
curl http://localhost:8080/api/users | jq '.'

echo -e "\n\n=== 3. Get User by ID (ID=1) ==="
curl http://localhost:8080/api/users/1 | jq '.'

echo -e "\n\n=== 4. Get User by Email ==="
curl http://localhost:8080/api/users/email/bob@example.com | jq '.'

echo -e "\n\n=== 5. Search Users by Name ==="
curl "http://localhost:8080/api/users/search?name=Bob" | jq '.'

echo -e "\n\n=== 6. Update User (ID=2) ==="
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Content-Type: application/json" \
  -d '{"name": "Bob Smith Updated", "email": "bob.updated@example.com", "phone": "9999999999", "address": "999 Updated St"}' | jq '.'

echo -e "\n\n=== 7. Get Active Users ==="
curl http://localhost:8080/api/users/active | jq '.'

echo -e "\n\n=== 8. Delete User (ID=3) ==="
curl -w "\nHTTP Status: %{http_code}\n" -X DELETE http://localhost:8080/api/users/3

echo -e "\n\n=== 9. Verify Deletion (should return 404) ==="
curl http://localhost:8080/api/users/3

echo -e "\n\n=== 10. Get All Users Again ==="
curl http://localhost:8080/api/users | jq '.'
```

---

## 📊 Quick Reference Table

| # | Method | Endpoint | cURL Command |
|---|--------|----------|--------------|
| 1 | POST | /api/users | `curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '{...}'` |
| 2 | GET | /api/users | `curl http://localhost:8080/api/users` |
| 3 | GET | /api/users/{id} | `curl http://localhost:8080/api/users/1` |
| 4 | PUT | /api/users/{id} | `curl -X PUT http://localhost:8080/api/users/1 -H "Content-Type: application/json" -d '{...}'` |
| 5 | DELETE | /api/users/{id} | `curl -X DELETE http://localhost:8080/api/users/1` |
| 6 | GET | /api/users/email/{email} | `curl http://localhost:8080/api/users/email/user@example.com` |
| 7 | GET | /api/users/active | `curl http://localhost:8080/api/users/active` |
| 8 | GET | /api/users/search?name={name} | `curl "http://localhost:8080/api/users/search?name=John"` |

---

## 💡 Tips & Tricks

### 1. Pretty Print JSON Responses
Install `jq` and pipe responses:
```bash
curl http://localhost:8080/api/users | jq '.'
```

### 2. Show HTTP Headers
```bash
curl -i http://localhost:8080/api/users
```

### 3. Show Only Status Code
```bash
curl -o /dev/null -s -w "%{http_code}\n" http://localhost:8080/api/users
```

### 4. Save Response to File
```bash
curl http://localhost:8080/api/users -o response.json
```

### 5. Verbose Output (Debug)
```bash
curl -v http://localhost:8080/api/users
```

### 6. Set Timeout
```bash
curl --max-time 10 http://localhost:8080/api/users
```

### 7. Follow Redirects
```bash
curl -L http://localhost:8080/api/users
```

### 8. Custom Headers
```bash
curl -H "Accept: application/json" -H "User-Agent: MyApp/1.0" http://localhost:8080/api/users
```

---

## 🚨 Common Error Responses

### 400 Bad Request (Validation Error)
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

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: '999'",
  "path": "/api/users/999"
}
```

### 409 Conflict (Duplicate Email)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "User already exists with email: 'john.doe@example.com'",
  "path": "/api/users"
}
```

---

## ✅ Testing Checklist

- [ ] Create a new user
- [ ] Get all users
- [ ] Get user by ID
- [ ] Get user by email
- [ ] Search users by name
- [ ] Get active users
- [ ] Update a user
- [ ] Delete a user
- [ ] Verify deletion (404 error)
- [ ] Test validation errors
- [ ] Test duplicate email error

---

**Note:** Make sure the application is running on `http://localhost:8080` before executing these commands.

For Windows users using Command Prompt, replace `\` with `^` for line continuation.
