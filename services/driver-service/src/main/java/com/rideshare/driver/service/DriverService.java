package com.rideshare.driver.service;

import com.rideshare.driver.document.DriverLocation;
import com.rideshare.driver.domain.Driver;
import com.rideshare.driver.repository.DriverLocationRepository;
import com.rideshare.driver.repository.DriverRepository;
import com.rideshare.driver.web.exception.DriverNotFound;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DriverService {

    private static final Logger log = LoggerFactory.getLogger(DriverService.class);

    private final DriverRepository driverRepository;
    private final DriverLocationRepository driverLocationRepository;

    private final Counter availabilityRegistrations;
    private final Counter locationUpdates;
    private final Counter driversAssigned;

    public DriverService(DriverRepository driverRepository,
                         DriverLocationRepository driverLocationRepository,
                         MeterRegistry meterRegistry) {
        this.driverRepository = driverRepository;
        this.driverLocationRepository = driverLocationRepository;

        this.availabilityRegistrations = Counter.builder("rideshare.drivers.availability.registered.total")
                .description("Total number of times a driver registered as available")
                .register(meterRegistry);

        this.locationUpdates = Counter.builder("rideshare.drivers.location.updated.total")
                .description("Total number of driver location updates")
                .register(meterRegistry);

        this.driversAssigned = Counter.builder("rideshare.drivers.assigned.total")
                .description("Total number of drivers assigned to an order")
                .register(meterRegistry);

        Gauge.builder("rideshare.drivers.available", driverRepository,
                        repo -> repo.countByStatus(Driver.DriverStatus.AVAILABLE))
                .description("Current number of drivers with AVAILABLE status")
                .register(meterRegistry);
    }

    @Transactional
    public void registerAvailability(Long userId) {
        Driver driver = driverRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("No driver profile found for userId={}, creating one", userId);
                    Driver d = new Driver();
                    d.setUserId(userId);
                    return d;
                });
        driver.setStatus(Driver.DriverStatus.AVAILABLE);
        log.info("Driver availability updated for user {} to {}", userId, driver.getStatus());
        driverRepository.save(driver);
        availabilityRegistrations.increment();
    }

    public void updateLocation(Long userId, double lat, double lng) {
        Driver driver = getDriverByUserId(userId);

        DriverLocation location = driverLocationRepository.findByDriverId(driver.getId())
                .map(existing -> {
                    existing.updatePosition(lat, lng);
                    return existing;
                })
                .orElse(new DriverLocation(driver.getId(), lat, lng));

        driverLocationRepository.save(location);
        locationUpdates.increment();
    }

    @Transactional(readOnly = true)
    public Driver getDriverByUserId(Long userId) {
        return driverRepository.findByUserId(userId)
                .orElseThrow(() -> new DriverNotFound(userId));
    }

    @Transactional
    public Optional<Driver> getNextAvailableDriver() {
        Optional<Driver> driverOpt = driverRepository.findFirstByStatusOrderByUpdatedAtAsc(Driver.DriverStatus.AVAILABLE);
        driverOpt.ifPresent(driver -> {
            driver.setStatus(Driver.DriverStatus.BUSY);
            driverRepository.save(driver);
            driversAssigned.increment();
            log.info("Driver {} marked as BUSY", driver.getId());
        });
        return driverOpt;
    }
}
