package com.satish.practicepostgreysql.service;

import com.satish.practicepostgreysql.document.UserAuditLog;
import com.satish.practicepostgreysql.enums.AuditOperation;
import com.satish.practicepostgreysql.enums.AuditStatus;
import com.satish.practicepostgreysql.repository.UserAuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service implementation for audit logging.
 * Logs all operations to MongoDB for audit trail.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final UserAuditLogRepository auditLogRepository;

    @Override
    public void logSuccess(Long userId, AuditOperation operation, String performedBy,
                           Map<String, Object> oldValues, Map<String, Object> newValues,
                           Map<String, Object> metadata) {
        try {
            UserAuditLog auditLog = UserAuditLog.builder()
                    .userId(userId)
                    .operation(operation.name())
                    .entityName("User")
                    .performedBy(performedBy != null ? performedBy : "SYSTEM")
                    .timestamp(LocalDateTime.now())
                    .oldValues(oldValues)
                    .newValues(newValues)
                    .metadata(metadata)
                    .status(AuditStatus.SUCCESS.name())
                    .requestId(UUID.randomUUID().toString())
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log saved successfully for operation: {} on userId: {}", operation, userId);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage(), e);
            // Don't throw exception - audit logging should not break the main flow
        }
    }

    @Override
    public void logFailure(Long userId, AuditOperation operation, String performedBy,
                           String errorMessage, Map<String, Object> metadata) {
        try {
            UserAuditLog auditLog = UserAuditLog.builder()
                    .userId(userId)
                    .operation(operation.name())
                    .entityName("User")
                    .performedBy(performedBy != null ? performedBy : "SYSTEM")
                    .timestamp(LocalDateTime.now())
                    .metadata(metadata)
                    .status(AuditStatus.FAILURE.name())
                    .errorMessage(errorMessage)
                    .requestId(UUID.randomUUID().toString())
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log saved for failed operation: {} on userId: {}", operation, userId);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<UserAuditLog> getAuditLogsByUserId(Long userId) {
        log.info("Fetching audit logs for userId: {}", userId);
        return auditLogRepository.findByUserId(userId);
    }

    @Override
    public List<UserAuditLog> getAuditLogsByOperation(String operation) {
        log.info("Fetching audit logs for operation: {}", operation);
        return auditLogRepository.findByOperation(operation);
    }

    @Override
    public List<UserAuditLog> getAuditLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Fetching audit logs between {} and {}", startTime, endTime);
        return auditLogRepository.findByTimestampBetween(startTime, endTime);
    }

    @Override
    public List<UserAuditLog> getAllAuditLogs() {
        log.info("Fetching all audit logs");
        return auditLogRepository.findAll();
    }
}
