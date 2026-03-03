package com.satish.practicepostgreysql.service;

import com.satish.practicepostgreysql.dto.UserRequestDTO;
import com.satish.practicepostgreysql.dto.UserResponseDTO;
import com.satish.practicepostgreysql.entity.User;
import com.satish.practicepostgreysql.enums.AuditOperation;
import com.satish.practicepostgreysql.exception.DuplicateResourceException;
import com.satish.practicepostgreysql.exception.ResourceNotFoundException;
import com.satish.practicepostgreysql.mapper.UserMapper;
import com.satish.practicepostgreysql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for User operations.
 * Implements business logic and transaction management.
 * Uses @Transactional for database transaction management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuditService auditService;

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        log.info("Creating new user with email: {}", requestDTO.getEmail());

        try {
            // Check if email already exists
            if (userRepository.existsByEmail(requestDTO.getEmail())) {
                log.error("User already exists with email: {}", requestDTO.getEmail());
                auditService.logFailure(null, AuditOperation.CREATE, requestDTO.getEmail(),
                        "Duplicate email", null);
                throw new DuplicateResourceException("User", "email", requestDTO.getEmail());
            }

            User user = userMapper.toEntity(requestDTO);
            User savedUser = userRepository.save(user);

            // Audit log for successful creation
            Map<String, Object> newValues = new HashMap<>();
            newValues.put("name", savedUser.getName());
            newValues.put("email", savedUser.getEmail());
            newValues.put("phone", savedUser.getPhone());
            newValues.put("address", savedUser.getAddress());
            newValues.put("active", savedUser.getActive());

            auditService.logSuccess(savedUser.getId(), AuditOperation.CREATE, requestDTO.getEmail(),
                    null, newValues, null);

            log.info("User created successfully with ID: {}", savedUser.getId());
            return userMapper.toResponseDTO(savedUser);
        } catch (DuplicateResourceException e) {
            throw e;
        } catch (Exception e) {
            auditService.logFailure(null, AuditOperation.CREATE, requestDTO.getEmail(),
                    e.getMessage(), null);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User", "id", id);
                });

        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");

        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());

        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        log.info("Updating user with ID: {}", id);

        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("User not found with ID: {}", id);
                        auditService.logFailure(id, AuditOperation.UPDATE, "SYSTEM",
                                "User not found", null);
                        return new ResourceNotFoundException("User", "id", id);
                    });

            // Store old values for audit
            Map<String, Object> oldValues = new HashMap<>();
            oldValues.put("name", existingUser.getName());
            oldValues.put("email", existingUser.getEmail());
            oldValues.put("phone", existingUser.getPhone());
            oldValues.put("address", existingUser.getAddress());
            oldValues.put("active", existingUser.getActive());

            // Check if email is being changed and if new email already exists
            if (!existingUser.getEmail().equals(requestDTO.getEmail()) &&
                    userRepository.existsByEmail(requestDTO.getEmail())) {
                log.error("Email already exists: {}", requestDTO.getEmail());
                auditService.logFailure(id, AuditOperation.UPDATE, requestDTO.getEmail(),
                        "Duplicate email", null);
                throw new DuplicateResourceException("User", "email", requestDTO.getEmail());
            }

            userMapper.updateEntityFromDTO(existingUser, requestDTO);
            User updatedUser = userRepository.save(existingUser);

            // Store new values for audit
            Map<String, Object> newValues = new HashMap<>();
            newValues.put("name", updatedUser.getName());
            newValues.put("email", updatedUser.getEmail());
            newValues.put("phone", updatedUser.getPhone());
            newValues.put("address", updatedUser.getAddress());
            newValues.put("active", updatedUser.getActive());

            auditService.logSuccess(updatedUser.getId(), AuditOperation.UPDATE, requestDTO.getEmail(),
                    oldValues, newValues, null);

            log.info("User updated successfully with ID: {}", updatedUser.getId());
            return userMapper.toResponseDTO(updatedUser);
        } catch (ResourceNotFoundException | DuplicateResourceException e) {
            throw e;
        } catch (Exception e) {
            auditService.logFailure(id, AuditOperation.UPDATE, "SYSTEM",
                    e.getMessage(), null);
            throw e;
        }
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("User not found with ID: {}", id);
                        auditService.logFailure(id, AuditOperation.DELETE, "SYSTEM",
                                "User not found", null);
                        return new ResourceNotFoundException("User", "id", id);
                    });

            // Store old values for audit
            Map<String, Object> oldValues = new HashMap<>();
            oldValues.put("name", user.getName());
            oldValues.put("email", user.getEmail());
            oldValues.put("phone", user.getPhone());
            oldValues.put("address", user.getAddress());
            oldValues.put("active", user.getActive());

            userRepository.delete(user);

            auditService.logSuccess(id, AuditOperation.DELETE, user.getEmail(),
                    oldValues, null, null);

            log.info("User deleted successfully with ID: {}", id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            auditService.logFailure(id, AuditOperation.DELETE, "SYSTEM",
                    e.getMessage(), null);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new ResourceNotFoundException("User", "email", email);
                });

        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveUsers() {
        log.info("Fetching all active users");

        List<User> activeUsers = userRepository.findByActive(true);
        log.info("Found {} active users", activeUsers.size());

        return activeUsers.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsersByName(String name) {
        log.info("Searching users by name: {}", name);

        List<User> users = userRepository.searchByName(name);
        log.info("Found {} users matching name: {}", users.size(), name);

        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
