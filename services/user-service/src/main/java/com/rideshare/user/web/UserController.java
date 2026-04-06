package com.rideshare.user.web;

import com.rideshare.user.service.UserService;
import com.rideshare.user.web.dto.LoginRequestDto;
import com.rideshare.user.web.dto.UserRequestDto;
import com.rideshare.user.web.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.rideshare.user.web.exception.UserNotFound;
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
        return UserResponseDto.fromUser(userService.registerUser(request));
    }

    @Operation(summary = "Login and receive a JWT token")
    @PostMapping(value = "/login", consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> login(@Valid @RequestBody LoginRequestDto request) {
        String token = userService.loginUser(request.getEmail(), request.getPassword());
        return Map.of("token", token);
    }

    @Operation(summary = "Get user by email")
    @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto getByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(UserResponseDto::fromUser)
                .orElseThrow(() -> new UserNotFound(email));
    }
}
