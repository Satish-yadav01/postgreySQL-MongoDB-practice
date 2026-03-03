package com.satish.practicepostgreysql.controller;

import com.satish.practicepostgreysql.document.UserAuditLog;
import com.satish.practicepostgreysql.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Audit Log operations.
 * Provides endpoints to query audit logs stored in MongoDB.
 * Base URL: /api/audit
 */
@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Slf4j
public class AuditController {

    private final AuditService auditService;

    /**
     * Get all audit logs.
     * GET /api/audit
     *
     * @return list of all audit logs
     */
    @GetMapping
    public ResponseEntity<List<UserAuditLog>> getAllAuditLogs() {
        log.info("REST request to get all audit logs");
        List<UserAuditLog> auditLogs = auditService.getAllAuditLogs();
        return ResponseEntity.ok(auditLogs);
    }

    /**
     * Get audit logs for a specific user.
     * GET /api/audit/user/{userId}
     *
     * @param userId the user ID
     * @return list of audit logs for the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAuditLog>> getAuditLogsByUserId(@PathVariable Long userId) {
        log.info("REST request to get audit logs for userId: {}", userId);
        List<UserAuditLog> auditLogs = auditService.getAuditLogsByUserId(userId);
        return ResponseEntity.ok(auditLogs);
    }

    /**
     * Get audit logs by operation type.
     * GET /api/audit/operation/{operation}
     *
     * @param operation the operation type (CREATE, UPDATE, DELETE, READ)
     * @return list of audit logs for the operation
     */
    @GetMapping("/operation/{operation}")
    public ResponseEntity<List<UserAuditLog>> getAuditLogsByOperation(@PathVariable String operation) {
        log.info("REST request to get audit logs for operation: {}", operation);
        List<UserAuditLog> auditLogs = auditService.getAuditLogsByOperation(operation.toUpperCase());
        return ResponseEntity.ok(auditLogs);
    }

    /**
     * Get audit logs within a time range.
     * GET /api/audit/timerange?startTime={startTime}&endTime={endTime}
     *
     * @param startTime start timestamp (format: yyyy-MM-dd'T'HH:mm:ss)
     * @param endTime   end timestamp (format: yyyy-MM-dd'T'HH:mm:ss)
     * @return list of audit logs within the time range
     */
    @GetMapping("/timerange")
    public ResponseEntity<List<UserAuditLog>> getAuditLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.info("REST request to get audit logs between {} and {}", startTime, endTime);
        List<UserAuditLog> auditLogs = auditService.getAuditLogsByTimeRange(startTime, endTime);
        return ResponseEntity.ok(auditLogs);
    }
}
