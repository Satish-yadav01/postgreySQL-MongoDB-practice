package com.satish.practicepostgreysql.controller;

import com.satish.practicepostgreysql.dto.UserRequestDTO;
import com.satish.practicepostgreysql.dto.UserResponseDTO;
import com.satish.practicepostgreysql.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User CRUD operations.
 * Follows RESTful API design principles.
 * Base URL: /api/users
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Create a new user.
     * POST /api/users
     *
     * @param requestDTO the user request data
     * @return created user with 201 status
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Validated @RequestBody UserRequestDTO requestDTO) {
        log.info("REST request to create user: {}", requestDTO.getEmail());
        UserResponseDTO response = userService.createUser(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get user by ID.
     * GET /api/users/{id}
     *
     * @param id the user ID
     * @return user response with 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("REST request to get user by ID: {}", id);
        UserResponseDTO response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all users.
     * GET /api/users
     *
     * @return list of all users with 200 status
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("REST request to get all users");
        List<UserResponseDTO> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    /**
     * Update existing user.
     * PUT /api/users/{id}
     *
     * @param id         the user ID
     * @param requestDTO the updated user data
     * @return updated user with 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Validated @RequestBody UserRequestDTO requestDTO) {
        log.info("REST request to update user with ID: {}", id);
        UserResponseDTO response = userService.updateUser(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete user by ID.
     * DELETE /api/users/{id}
     *
     * @param id the user ID
     * @return 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("REST request to delete user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get user by email.
     * GET /api/users/email/{email}
     *
     * @param email the email address
     * @return user response with 200 status
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        log.info("REST request to get user by email: {}", email);
        UserResponseDTO response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all active users.
     * GET /api/users/active
     *
     * @return list of active users with 200 status
     */
    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
        log.info("REST request to get all active users");
        List<UserResponseDTO> response = userService.getActiveUsers();
        return ResponseEntity.ok(response);
    }

    /**
     * Search users by name.
     * GET /api/users/search?name={name}
     *
     * @param name the name search term
     * @return list of matching users with 200 status
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByName(@RequestParam String name) {
        log.info("REST request to search users by name: {}", name);
        List<UserResponseDTO> response = userService.searchUsersByName(name);
        return ResponseEntity.ok(response);
    }
}
