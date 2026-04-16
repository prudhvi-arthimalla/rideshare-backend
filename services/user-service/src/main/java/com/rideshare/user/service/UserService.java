package com.rideshare.user.service;

import com.rideshare.commons.security.JwtTokenProvider;
import com.rideshare.user.domain.User;
import com.rideshare.user.repository.UserRepository;
import com.rideshare.user.web.dto.UserRequestDto;
import com.rideshare.user.web.exception.UnableToLogin;
import com.rideshare.user.web.exception.UserAlreadyExists;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExists("email", request.getEmail());
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExists("phone number", request.getPhoneNumber());
        }
        var passwordHash = passwordEncoder.encode(request.getPassword());
        User user = User.toUser(request, passwordHash);
        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UnableToLogin::new);
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UnableToLogin();
        }
        return jwtTokenProvider.getToken(user.getEmail(), user.getRole().name(), user.getId());
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
