package com.satish.practicepostgreysql.service;

import com.satish.practicepostgreysql.dto.UserRequestDTO;
import com.satish.practicepostgreysql.dto.UserResponseDTO;

import java.util.List;

/**
 * Service interface for User operations.
 * Defines the contract for user-related business logic.
 */
public interface UserService {

    /**
     * Create a new user.
     *
     * @param requestDTO the user request data
     * @return created user response
     */
    UserResponseDTO createUser(UserRequestDTO requestDTO);

    /**
     * Get user by ID.
     *
     * @param id the user ID
     * @return user response
     */
    UserResponseDTO getUserById(Long id);

    /**
     * Get all users.
     *
     * @return list of all users
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Update existing user.
     *
     * @param id         the user ID
     * @param requestDTO the updated user data
     * @return updated user response
     */
    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);

    /**
     * Delete user by ID.
     *
     * @param id the user ID
     */
    void deleteUser(Long id);

    /**
     * Get user by email.
     *
     * @param email the email address
     * @return user response
     */
    UserResponseDTO getUserByEmail(String email);

    /**
     * Get all active users.
     *
     * @return list of active users
     */
    List<UserResponseDTO> getActiveUsers();

    /**
     * Search users by name.
     *
     * @param name the name search term
     * @return list of matching users
     */
    List<UserResponseDTO> searchUsersByName(String name);
}
