package com.rideshare.user.web;

import com.rideshare.commons.dto.user.LoginRequestDto;
import com.rideshare.commons.dto.user.UserRequestDto;
import com.rideshare.commons.dto.user.UserResponseDto;
import com.rideshare.user.domain.User;
import com.rideshare.user.service.UserService;
import com.rideshare.user.web.examples.RequestExample;
import com.rideshare.user.web.examples.ResponseExample;
import com.rideshare.user.web.exception.UserNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserController.class);



    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user.",
            description = "Register a new rider or driver.",
            responses = {
                    @ApiResponse(
                            description = "Successfully created a new user.",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponseDto.class),
                                    examples = @ExampleObject(ResponseExample.USER_RESPONSE)
                            )
                    ),
                    @ApiResponse(
                            description = "Invalid Request",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class),
                                    examples = @ExampleObject()
                            )
                    )
            }
    )
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid
                                    @RequestBody
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                            description = "New user registration payload.",
                                            required = true,
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = UserRequestDto.class),
                                                    examples = @ExampleObject(RequestExample.REGISTER_REQUEST)
                                            )
                                    ) UserRequestDto request) {
        log.info("Received request to register a new user");
        return userService.registerUser(request).toTransferObject();
    }

    @Operation(
            summary = "Login and receive a JWT token",
            description = "Authenticate with email and password. Returns a signed JWT to include as a Bearer token in subsequent requests.",
            responses = {
                    @ApiResponse(
                            description = "Successfully authenticated.",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(ResponseExample.LOGIN_RESPONSE)
                            )
                    ),
                    @ApiResponse(
                            description = "Invalid credentials.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    )
            }
    )
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> login(@Valid
                                     @RequestBody
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                             description = "Login credentials.",
                                             required = true,
                                             content = @Content(
                                                     mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                     schema = @Schema(implementation = LoginRequestDto.class),
                                                     examples = @ExampleObject(RequestExample.LOGIN_REQUEST)
                                             )
                                     ) LoginRequestDto request) {
        log.info("Received request to login with username and password");
        String token = userService.loginUser(request.getEmail(), request.getPassword());
        return Map.of("token", token);
    }

    @Operation(
            summary = "Get the currently authenticated user",
            description = "Returns the profile of the user associated with the provided JWT token.",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved user profile.",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponseDto.class),
                                    examples = @ExampleObject(ResponseExample.USER_RESPONSE)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    )
            }
    )
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto getMe(@AuthenticationPrincipal String email) {
        log.info("Received request to get current user");
        return userService.getUserByEmail(email)
                .map(User::toTransferObject)
                .orElseThrow(() -> new UserNotFound(email));
    }
}
