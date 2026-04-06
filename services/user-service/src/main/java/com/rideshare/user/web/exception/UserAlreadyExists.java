package com.rideshare.user.web.exception;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String field, String value) {
        super("User already exists with " + field + ": " + value);
    }
}
