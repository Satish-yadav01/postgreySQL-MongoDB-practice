package com.satish.practicepostgreysql.mapper;

import com.satish.practicepostgreysql.dto.UserRequestDTO;
import com.satish.practicepostgreysql.dto.UserResponseDTO;
import com.satish.practicepostgreysql.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper class to convert between User entity and DTOs.
 * Follows the Mapper design pattern for separation of concerns.
 */
@Component
public class UserMapper {

    /**
     * Convert UserRequestDTO to User entity.
     *
     * @param requestDTO the request DTO
     * @return User entity
     */
    public User toEntity(UserRequestDTO requestDTO) {
        return User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .phone(requestDTO.getPhone())
                .address(requestDTO.getAddress())
                .active(requestDTO.getActive() != null ? requestDTO.getActive() : true)
                .build();
    }

    /**
     * Convert User entity to UserResponseDTO.
     *
     * @param user the user entity
     * @return UserResponseDTO
     */
    public UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Update existing User entity with data from UserRequestDTO.
     *
     * @param user       the existing user entity
     * @param requestDTO the request DTO with updated data
     */
    public void updateEntityFromDTO(User user, UserRequestDTO requestDTO) {
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());
        user.setAddress(requestDTO.getAddress());
        if (requestDTO.getActive() != null) {
            user.setActive(requestDTO.getActive());
        }
    }
}
