package com.rideshare.user.service;

import com.rideshare.commons.dto.user.UserRequestDto;
import com.rideshare.commons.security.JwtTokenProvider;
import com.rideshare.user.domain.User;
import com.rideshare.user.repository.UserRepository;
import com.rideshare.user.web.exception.UnableToLogin;
import com.rideshare.user.web.exception.UserAlreadyExists;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MeterRegistry meterRegistry;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // Counters are pre-built for each known role so we avoid tag-value allocation on the hot path
    private final Counter riderRegistrations;
    private final Counter driverRegistrations;
    private final Counter loginSuccess;
    private final Counter loginFailure;

    public UserService(UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider,
                       BCryptPasswordEncoder passwordEncoder,
                       MeterRegistry meterRegistry) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.meterRegistry = meterRegistry;

        this.riderRegistrations  = registrationCounter("RIDER");
        this.driverRegistrations = registrationCounter("DRIVER");
        this.loginSuccess        = loginCounter("success");
        this.loginFailure        = loginCounter("failure");
    }

    private Counter registrationCounter(String role) {
        return Counter.builder("rideshare.users.registered.total")
                .description("Total number of user registrations")
                .tag("role", role)
                .register(meterRegistry);
    }

    private Counter loginCounter(String result) {
        return Counter.builder("rideshare.users.login.total")
                .description("Total number of login attempts")
                .tag("result", result)
                .register(meterRegistry);
    }

    public User registerUser(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("User with email {} already exists", request.getEmail());
            throw new UserAlreadyExists("email", request.getEmail());
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            log.warn("User with phone number {} already exists", request.getPhoneNumber());
            throw new UserAlreadyExists("phone number", request.getPhoneNumber());
        }
        var passwordHash = passwordEncoder.encode(request.getPassword());
        log.info("Saving user {} {} to repository", request.getFirstName(), request.getLastName());
        User saved = userRepository.save(User.fromTransferObject(request, passwordHash));

        if (saved.getRole() == User.Role.DRIVER) {
            driverRegistrations.increment();
        } else {
            riderRegistrations.increment();
        }
        return saved;
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    loginFailure.increment();
                    return new UnableToLogin();
                });
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            log.warn("Invalid password for user {}", user.getEmail());
            loginFailure.increment();
            throw new UnableToLogin();
        }
        loginSuccess.increment();
        return jwtTokenProvider.getToken(user.getEmail(), user.getRole().name(), user.getId());
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
