package com.rideshare.user.web.exception;

public class UnableToLogin extends RuntimeException {
    public UnableToLogin() {
        super("Invalid email or password");
    }
}
