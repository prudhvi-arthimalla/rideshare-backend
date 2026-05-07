package com.rideshare.driver.web;

import com.rideshare.commons.dto.driver.DriverResponse;
import com.rideshare.commons.dto.driver.UpdateLocationRequest;
import com.rideshare.driver.service.DriverService;
import com.rideshare.driver.web.examples.RequestExample;
import com.rideshare.driver.web.examples.ResponseExample;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
@Tag(name = "Drivers", description = "Driver availability and location management")
public class DriverController {

    private static final Logger log = LoggerFactory.getLogger(DriverController.class);

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @Operation(
            summary = "Register driver availability",
            description = "Mark the authenticated driver as available for new ride requests. Creates a driver profile if one does not exist yet.",
            responses = {
                    @ApiResponse(
                            description = "Availability registered successfully.",
                            responseCode = "204"
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
    @PostMapping(value = "/register-availability")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerDriverAvailability(Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        log.info("Registering driver availability for userId={}", userId);
        driverService.registerAvailability(userId);
    }

    @Operation(
            summary = "Update driver's current location",
            description = "Update the authenticated driver's current GPS coordinates. Latitude must be between -90 and 90; longitude between -180 and 180.",
            responses = {
                    @ApiResponse(
                            description = "Location updated successfully.",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Invalid coordinates.",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
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
    @PutMapping(value = "/location", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDriverCurrentLocation(@Valid
                                            @RequestBody
                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                    description = "Driver's current GPS coordinates.",
                                                    required = true,
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema = @Schema(implementation = UpdateLocationRequest.class),
                                                            examples = @ExampleObject(RequestExample.UPDATE_LOCATION_REQUEST)
                                                    )
                                            ) UpdateLocationRequest request,
                                            Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        log.info("Updating location for userId={}", userId);
        driverService.updateLocation(userId, request.getLat(), request.getLng());
    }

    @Operation(
            summary = "Get current driver's profile",
            description = "Returns the authenticated driver's profile including their current status.",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved driver profile.",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DriverResponse.class),
                                    examples = @ExampleObject(ResponseExample.DRIVER_RESPONSE)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    ),
                    @ApiResponse(
                            description = "Driver profile not found.",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    )
            }
    )
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public DriverResponse getMyProfile(Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        log.info("Fetching driver profile for userId={}", userId);
        return driverService.getDriverByUserId(userId).toTransferObject();
    }
}
