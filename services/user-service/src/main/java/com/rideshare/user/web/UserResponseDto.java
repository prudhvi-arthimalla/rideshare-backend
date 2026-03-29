package com.rideshare.user.web;

import com.rideshare.user.repository.User;
import com.rideshare.user.repository.User.Role;

import java.time.Instant;

public class UserResponseDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public static UserResponseDto fromUser(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.id = user.getId();
        dto.email = user.getEmail();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.phoneNumber = user.getPhoneNumber();
        dto.role = user.getRole();
        dto.createdAt = user.getCreatedAt();
        return dto;
    }
}
