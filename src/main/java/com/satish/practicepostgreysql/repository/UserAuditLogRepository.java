package com.satish.practicepostgreysql.repository;

import com.satish.practicepostgreysql.document.UserAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB Repository for UserAuditLog.
 * Provides methods to query audit logs.
 */
@Repository
public interface UserAuditLogRepository extends MongoRepository<UserAuditLog, String> {

    /**
     * Find all audit logs for a specific user.
     *
     * @param userId the user ID
     * @return list of audit logs
     */
    List<UserAuditLog> findByUserId(Long userId);

    /**
     * Find audit logs by operation type.
     *
     * @param operation the operation type (CREATE, UPDATE, DELETE, READ)
     * @return list of audit logs
     */
    List<UserAuditLog> findByOperation(String operation);

    /**
     * Find audit logs by performer.
     *
     * @param performedBy the username or email of performer
     * @return list of audit logs
     */
    List<UserAuditLog> findByPerformedBy(String performedBy);

    /**
     * Find audit logs within a time range.
     *
     * @param startTime start timestamp
     * @param endTime   end timestamp
     * @return list of audit logs
     */
    List<UserAuditLog> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Find audit logs by user ID and operation.
     *
     * @param userId    the user ID
     * @param operation the operation type
     * @return list of audit logs
     */
    List<UserAuditLog> findByUserIdAndOperation(Long userId, String operation);

    /**
     * Find audit logs by request ID.
     *
     * @param requestId the request ID
     * @return list of audit logs
     */
    List<UserAuditLog> findByRequestId(String requestId);

    /**
     * Find audit logs by status.
     *
     * @param status the status (SUCCESS, FAILURE)
     * @return list of audit logs
     */
    List<UserAuditLog> findByStatus(String status);
}
