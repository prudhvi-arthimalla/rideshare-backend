package com.rideshare.user.service;

import com.rideshare.user.repository.User;
import com.rideshare.user.repository.UserRepository;
import com.rideshare.user.web.UserRequestDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRequestDto request) {
        var passwordHash = passwordEncoder.encode(request.getPassword());
        User user = User.toUser(request, passwordHash);
        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getPasswordHash().matches(passwordEncoder.encode(password))){

        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

}
