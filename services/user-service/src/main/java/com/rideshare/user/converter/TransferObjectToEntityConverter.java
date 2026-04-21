package com.rideshare.user.converter;

import com.rideshare.commons.dto.user.UserRequestDto;
import com.rideshare.user.domain.User;

public class TransferObjectToEntityConverter {

    public static User convert(UserRequestDto dto, String passwordHash) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordHash);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(User.Role.valueOf(dto.getRole().name()));
        return user;
    }

    private TransferObjectToEntityConverter() {}
}
