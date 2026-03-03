package com.satish.practicepostgreysql.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * MongoDB Document for storing audit logs.
 * Stores all CRUD operations performed on User entity.
 * Uses MongoDB for better performance and scalability for audit data.
 */
@Document(collection = "user_audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuditLog {

    @Id
    private String id;

    /**
     * User ID from PostgreSQL database
     */
    @Indexed
    private Long userId;

    /**
     * Operation type: CREATE, UPDATE, DELETE, READ
     */
    @Indexed
    private String operation;

    /**
     * Entity name (e.g., "User")
     */
    private String entityName;

    /**
     * User who performed the action (can be username, email, or system)
     */
    @Indexed
    private String performedBy;

    /**
     * Timestamp when the operation was performed
     */
    @Indexed
    private LocalDateTime timestamp;

    /**
     * Old values before the operation (for UPDATE and DELETE)
     */
    private Map<String, Object> oldValues;

    /**
     * New values after the operation (for CREATE and UPDATE)
     */
    private Map<String, Object> newValues;

    /**
     * IP address of the client
     */
    private String ipAddress;

    /**
     * Additional metadata or context
     */
    private Map<String, Object> metadata;

    /**
     * Status of the operation: SUCCESS, FAILURE
     */
    private String status;

    /**
     * Error message if operation failed
     */
    private String errorMessage;

    /**
     * Request ID for tracing
     */
    @Indexed
    private String requestId;
}
