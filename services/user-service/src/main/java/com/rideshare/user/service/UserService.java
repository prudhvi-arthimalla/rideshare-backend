package com.rideshare.user.service;

import com.rideshare.commons.dto.user.UserRequestDto;
import com.rideshare.commons.security.JwtTokenProvider;
import com.rideshare.user.domain.User;
import com.rideshare.user.repository.UserRepository;
import com.rideshare.user.web.exception.UnableToLogin;
import com.rideshare.user.web.exception.UserAlreadyExists;
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
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
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
        return userRepository.save(User.fromTransferObject(request, passwordHash));
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UnableToLogin::new);
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            log.warn("Invalid password for user {}", user.getEmail());
            throw new UnableToLogin();
        }
        return jwtTokenProvider.getToken(user.getEmail(), user.getRole().name(), user.getId());
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
