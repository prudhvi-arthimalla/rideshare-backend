package com.rideshare.user.converter;

import com.rideshare.commons.dto.user.UserResponseDto;
import com.rideshare.commons.dto.user.UserRole;
import com.rideshare.user.domain.User;

public class EntityToTransferObjectConverter {

    public static UserResponseDto convert(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(UserRole.valueOf(user.getRole().name()));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    private EntityToTransferObjectConverter() {}
}
