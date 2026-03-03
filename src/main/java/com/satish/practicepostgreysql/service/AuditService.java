package com.satish.practicepostgreysql.service;

import com.satish.practicepostgreysql.document.UserAuditLog;
import com.satish.practicepostgreysql.enums.AuditOperation;
import com.satish.practicepostgreysql.enums.AuditStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for audit logging operations.
 */
public interface AuditService {

    /**
     * Log a successful operation.
     *
     * @param userId      the user ID
     * @param operation   the operation type
     * @param performedBy who performed the operation
     * @param oldValues   old values (for UPDATE/DELETE)
     * @param newValues   new values (for CREATE/UPDATE)
     * @param metadata    additional metadata
     */
    void logSuccess(Long userId, AuditOperation operation, String performedBy,
                    Map<String, Object> oldValues, Map<String, Object> newValues,
                    Map<String, Object> metadata);

    /**
     * Log a failed operation.
     *
     * @param userId       the user ID
     * @param operation    the operation type
     * @param performedBy  who performed the operation
     * @param errorMessage the error message
     * @param metadata     additional metadata
     */
    void logFailure(Long userId, AuditOperation operation, String performedBy,
                    String errorMessage, Map<String, Object> metadata);

    /**
     * Get all audit logs for a specific user.
     *
     * @param userId the user ID
     * @return list of audit logs
     */
    List<UserAuditLog> getAuditLogsByUserId(Long userId);

    /**
     * Get audit logs by operation type.
     *
     * @param operation the operation type
     * @return list of audit logs
     */
    List<UserAuditLog> getAuditLogsByOperation(String operation);

    /**
     * Get audit logs within a time range.
     *
     * @param startTime start timestamp
     * @param endTime   end timestamp
     * @return list of audit logs
     */
    List<UserAuditLog> getAuditLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Get all audit logs.
     *
     * @return list of all audit logs
     */
    List<UserAuditLog> getAllAuditLogs();
}
