package com.rideshare.driver.web.exception;

public class DriverNotFound extends RuntimeException {

    public DriverNotFound(Long userId) {
        super("Driver profile not found for userId: " + userId);
    }
}
