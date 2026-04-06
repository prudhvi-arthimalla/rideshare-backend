package com.rideshare.user.web.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String email) {
        super("User not found: " + email);
    }
}
