package com.rideshare.user.web;

import com.rideshare.commons.dto.user.LoginRequestDto;
import com.rideshare.commons.dto.user.UserRequestDto;
import com.rideshare.commons.dto.user.UserResponseDto;
import com.rideshare.user.service.UserService;
import com.rideshare.user.web.exception.UserNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User registration and authentication")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody UserRequestDto request) {
        return userService.registerUser(request).toTransferObject();
    }

    @Operation(summary = "Login and receive a JWT token")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> login(@Valid @RequestBody LoginRequestDto request) {
        String token = userService.loginUser(request.getEmail(), request.getPassword());
        return Map.of("token", token);
    }

    @Operation(summary = "Get the currently authenticated user")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto getMe(@AuthenticationPrincipal String email) {
        return userService.getUserByEmail(email)
                .map(user -> user.toTransferObject())
                .orElseThrow(() -> new UserNotFound(email));
    }
}
