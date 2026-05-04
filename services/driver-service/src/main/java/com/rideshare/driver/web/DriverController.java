package com.rideshare.driver.web;

import com.rideshare.commons.dto.driver.DriverResponse;
import com.rideshare.commons.dto.driver.UpdateLocationRequest;
import com.rideshare.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
@Tag(name = "Drivers", description = "Driver actions")
public class DriverController {

    private static final Logger log = LoggerFactory.getLogger(DriverController.class);

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @Operation(summary = "Register driver availability")
    @PostMapping(value = "/register-availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerDriverAvailability(Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        log.info("Registering driver availability for userId={}", userId);
        driverService.registerAvailability(userId);
    }

    @Operation(summary = "Update driver's current location")
    @PutMapping(value = "/location", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDriverCurrentLocation(@Valid @RequestBody UpdateLocationRequest request,
                                            Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        log.info("Updating location for userId={}", userId);
        driverService.updateLocation(userId, request.getLat(), request.getLng());
    }

    @Operation(summary = "Get current driver's profile")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public DriverResponse getMyProfile(Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        log.info("Fetching driver profile for userId={}", userId);
        return driverService.getDriverByUserId(userId).toTransferObject();
    }
}
