# 🔍 Audit API - cURL Commands

Complete cURL commands for all Audit Log endpoints.

---

## 📋 Audit Endpoints (4 Total)

### Base URL: `http://localhost:8080/api/audit`

---

## 1. Get All Audit Logs

**Endpoint:** `GET /api/audit`

### Basic Request
```bash
curl http://localhost:8080/api/audit
```

### With Pretty Print
```bash
curl http://localhost:8080/api/audit | jq '.'
```

### Expected Response
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "userId": 1,
    "operation": "CREATE",
    "entityName": "User",
    "performedBy": "john.doe@example.com",
    "timestamp": "2024-01-15T10:30:00",
    "oldValues": null,
    "newValues": {
      "name": "John Doe",
      "email": "john.doe@example.com",
      "phone": "1234567890",
      "address": "123 Main St",
      "active": true
    },
    "ipAddress": null,
    "metadata": null,
    "status": "SUCCESS",
    "errorMessage": null,
    "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  },
  {
    "id": "507f1f77bcf86cd799439012",
    "userId": 1,
    "operation": "UPDATE",
    "entityName": "User",
    "performedBy": "john.updated@example.com",
    "timestamp": "2024-01-15T11:00:00",
    "oldValues": {
      "name": "John Doe",
      "email": "john.doe@example.com",
      "phone": "1234567890"
    },
    "newValues": {
      "name": "John Doe Updated",
      "email": "john.updated@example.com",
      "phone": "9999999999"
    },
    "status": "SUCCESS"
  }
]
```

---

## 2. Get Audit Logs for Specific User

**Endpoint:** `GET /api/audit/user/{userId}`

### Get Logs for User ID 1
```bash
curl http://localhost:8080/api/audit/user/1
```

### Get Logs for User ID 2
```bash
curl http://localhost:8080/api/audit/user/2
```

### Get Logs for User ID 5
```bash
curl http://localhost:8080/api/audit/user/5
```

### With Pretty Print
```bash
curl http://localhost:8080/api/audit/user/1 | jq '.'
```

### Count Audit Logs for User
```bash
curl http://localhost:8080/api/audit/user/1 | jq 'length'
```

### Get Only Operations for User
```bash
curl http://localhost:8080/api/audit/user/1 | jq '.[].operation'
```

### Expected Response
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "userId": 1,
    "operation": "CREATE",
    "performedBy": "john.doe@example.com",
    "timestamp": "2024-01-15T10:30:00",
    "newValues": {
      "name": "John Doe",
      "email": "john.doe@example.com"
    },
    "status": "SUCCESS"
  },
  {
    "id": "507f1f77bcf86cd799439012",
    "userId": 1,
    "operation": "UPDATE",
    "performedBy": "john.updated@example.com",
    "timestamp": "2024-01-15T11:00:00",
    "oldValues": {...},
    "newValues": {...},
    "status": "SUCCESS"
  },
  {
    "id": "507f1f77bcf86cd799439013",
    "userId": 1,
    "operation": "DELETE",
    "performedBy": "admin@example.com",
    "timestamp": "2024-01-15T12:00:00",
    "oldValues": {...},
    "status": "SUCCESS"
  }
]
```

---

## 3. Get Audit Logs by Operation Type

**Endpoint:** `GET /api/audit/operation/{operation}`

### Get All CREATE Operations
```bash
curl http://localhost:8080/api/audit/operation/CREATE
```

### Get All UPDATE Operations
```bash
curl http://localhost:8080/api/audit/operation/UPDATE
```

### Get All DELETE Operations
```bash
curl http://localhost:8080/api/audit/operation/DELETE
```

### Get All READ Operations
```bash
curl http://localhost:8080/api/audit/operation/READ
```

### With Pretty Print
```bash
curl http://localhost:8080/api/audit/operation/CREATE | jq '.'
```

### Count Operations
```bash
# Count CREATE operations
curl http://localhost:8080/api/audit/operation/CREATE | jq 'length'

# Count UPDATE operations
curl http://localhost:8080/api/audit/operation/UPDATE | jq 'length'

# Count DELETE operations
curl http://localhost:8080/api/audit/operation/DELETE | jq 'length'
```

### Expected Response (CREATE operations)
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "userId": 1,
    "operation": "CREATE",
    "performedBy": "john.doe@example.com",
    "timestamp": "2024-01-15T10:30:00",
    "newValues": {
      "name": "John Doe",
      "email": "john.doe@example.com"
    },
    "status": "SUCCESS"
  },
  {
    "id": "507f1f77bcf86cd799439014",
    "userId": 2,
    "operation": "CREATE",
    "performedBy": "jane.smith@example.com",
    "timestamp": "2024-01-15T10:45:00",
    "newValues": {
      "name": "Jane Smith",
      "email": "jane.smith@example.com"
    },
    "status": "SUCCESS"
  }
]
```

---

## 4. Get Audit Logs by Time Range

**Endpoint:** `GET /api/audit/timerange?startTime={startTime}&endTime={endTime}`

**Time Format:** `yyyy-MM-dd'T'HH:mm:ss`

### Get Logs for Today
```bash
curl "http://localhost:8080/api/audit/timerange?startTime=2024-01-15T00:00:00&endTime=2024-01-15T23:59:59"
```

### Get Logs for Last Week
```bash
curl "http://localhost:8080/api/audit/timerange?startTime=2024-01-08T00:00:00&endTime=2024-01-15T23:59:59"
```

### Get Logs for Last Month
```bash
curl "http://localhost:8080/api/audit/timerange?startTime=2023-12-15T00:00:00&endTime=2024-01-15T23:59:59"
```

### Get Logs for Specific Hour
```bash
curl "http://localhost:8080/api/audit/timerange?startTime=2024-01-15T10:00:00&endTime=2024-01-15T11:00:00"
```

### With Pretty Print
```bash
curl "http://localhost:8080/api/audit/timerange?startTime=2024-01-15T00:00:00&endTime=2024-01-15T23:59:59" | jq '.'
```

### Expected Response
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "userId": 1,
    "operation": "CREATE",
    "timestamp": "2024-01-15T10:30:00",
    "status": "SUCCESS"
  },
  {
    "id": "507f1f77bcf86cd799439012",
    "userId": 1,
    "operation": "UPDATE",
    "timestamp": "2024-01-15T11:00:00",
    "status": "SUCCESS"
  }
]
```

---

## 🔄 Complete Audit Workflow Example

### Step 1: Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "phone": "1234567890"
  }'
```

### Step 2: Check Audit Log for Creation
```bash
curl http://localhost:8080/api/audit/user/1 | jq '.'
```

### Step 3: Update the User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User Updated",
    "email": "test.updated@example.com",
    "phone": "9999999999"
  }'
```

### Step 4: Check Audit Log for Update
```bash
curl http://localhost:8080/api/audit/user/1 | jq '.'
```

### Step 5: Delete the User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### Step 6: Check Complete Audit History
```bash
curl http://localhost:8080/api/audit/user/1 | jq '.'
```

**Expected: Shows CREATE, UPDATE, and DELETE operations**

---

## 📊 Advanced Queries with jq

### Get Only Successful Operations
```bash
curl http://localhost:8080/api/audit | jq '.[] | select(.status == "SUCCESS")'
```

### Get Only Failed Operations
```bash
curl http://localhost:8080/api/audit | jq '.[] | select(.status == "FAILURE")'
```

### Get Operations by Specific User Email
```bash
curl http://localhost:8080/api/audit | jq '.[] | select(.performedBy == "john.doe@example.com")'
```

### Get Latest 5 Audit Logs
```bash
curl http://localhost:8080/api/audit | jq 'sort_by(.timestamp) | reverse | .[0:5]'
```

### Count Operations by Type
```bash
# Count CREATE operations
curl http://localhost:8080/api/audit/operation/CREATE | jq 'length'

# Count UPDATE operations
curl http://localhost:8080/api/audit/operation/UPDATE | jq 'length'

# Count DELETE operations
curl http://localhost:8080/api/audit/operation/DELETE | jq 'length'
```

### Get Audit Logs with Error Messages
```bash
curl http://localhost:8080/api/audit | jq '.[] | select(.errorMessage != null)'
```

---

## 🔍 MongoDB Shell Queries

### Connect to MongoDB
```bash
mongosh
use audit_db
```

### Get All Audit Logs
```javascript
db.user_audit_logs.find().pretty()
```

### Get Logs for Specific User
```javascript
db.user_audit_logs.find({ userId: 1 }).pretty()
```

### Get Logs by Operation
```javascript
db.user_audit_logs.find({ operation: "CREATE" }).pretty()
```

### Get Failed Operations
```javascript
db.user_audit_logs.find({ status: "FAILURE" }).pretty()
```

### Get Logs by Date Range
```javascript
db.user_audit_logs.find({
  timestamp: {
    $gte: ISODate("2024-01-01T00:00:00Z"),
    $lte: ISODate("2024-12-31T23:59:59Z")
  }
}).pretty()
```

### Count Audit Logs
```javascript
db.user_audit_logs.countDocuments()
```

### Count by Operation
```javascript
db.user_audit_logs.aggregate([
  { $group: { _id: "$operation", count: { $sum: 1 } } }
])
```

### Get Latest 10 Logs
```javascript
db.user_audit_logs.find().sort({ timestamp: -1 }).limit(10).pretty()
```

---

## 📈 Monitoring Commands

### Check Total Audit Logs
```bash
curl http://localhost:8080/api/audit | jq 'length'
```

### Check Logs for Today
```bash
TODAY=$(date +%Y-%m-%d)
curl "http://localhost:8080/api/audit/timerange?startTime=${TODAY}T00:00:00&endTime=${TODAY}T23:59:59" | jq 'length'
```

### Check Failed Operations
```bash
curl http://localhost:8080/api/audit | jq '[.[] | select(.status == "FAILURE")] | length'
```

### Check Operations by User
```bash
curl http://localhost:8080/api/audit/user/1 | jq 'length'
```

---

## 🎯 Quick Reference Table

| Endpoint | Method | Purpose | Example |
|----------|--------|---------|---------|
| /api/audit | GET | Get all logs | `curl http://localhost:8080/api/audit` |
| /api/audit/user/{userId} | GET | Get logs for user | `curl http://localhost:8080/api/audit/user/1` |
| /api/audit/operation/{operation} | GET | Get logs by operation | `curl http://localhost:8080/api/audit/operation/CREATE` |
| /api/audit/timerange | GET | Get logs by time | `curl "http://localhost:8080/api/audit/timerange?startTime=...&endTime=..."` |

---

## 💡 Tips

1. **Use jq for Pretty Print**: Install jq for better JSON formatting
   ```bash
   # Ubuntu/Debian
   sudo apt-get install jq
   
   # macOS
   brew install jq
   ```

2. **Save Responses to File**:
   ```bash
   curl http://localhost:8080/api/audit > audit_logs.json
   ```

3. **Check Response Status**:
   ```bash
   curl -w "\nHTTP Status: %{http_code}\n" http://localhost:8080/api/audit
   ```

4. **Verbose Output**:
   ```bash
   curl -v http://localhost:8080/api/audit
   ```

---

## ✅ Testing Checklist

- [ ] Get all audit logs
- [ ] Get logs for specific user
- [ ] Get logs by CREATE operation
- [ ] Get logs by UPDATE operation
- [ ] Get logs by DELETE operation
- [ ] Get logs by time range
- [ ] Verify audit log structure
- [ ] Check MongoDB directly
- [ ] Test with jq filters
- [ ] Verify timestamps are correct

---

**Note:** Make sure both the application and MongoDB are running before executing these commands.

For Windows users using Command Prompt, replace `\` with `^` for line continuation.
