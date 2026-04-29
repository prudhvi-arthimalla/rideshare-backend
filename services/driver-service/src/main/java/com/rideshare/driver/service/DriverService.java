package com.rideshare.driver.service;

import com.rideshare.driver.document.DriverLocation;
import com.rideshare.driver.domain.Driver;
import com.rideshare.driver.repository.DriverLocationRepository;
import com.rideshare.driver.repository.DriverRepository;
import com.rideshare.driver.web.exception.DriverNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverService {

    private static final Logger log = LoggerFactory.getLogger(DriverService.class);

    private final DriverRepository driverRepository;
    private final DriverLocationRepository driverLocationRepository;

    public DriverService(DriverRepository driverRepository, DriverLocationRepository driverLocationRepository) {
        this.driverRepository = driverRepository;
        this.driverLocationRepository = driverLocationRepository;
    }

    @Transactional
    public Driver registerAvailability(Long userId) {
        Driver driver = driverRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("No driver profile found for userId={}, creating one", userId);
                    Driver d = new Driver();
                    d.setUserId(userId);
                    return d;
                });
        driver.setStatus(Driver.DriverStatus.AVAILABLE);
        log.info("Driver availability updated for user {} to {}", userId, driver.getStatus());
        return driverRepository.save(driver);
    }

    public DriverLocation updateLocation(Long userId, double lat, double lng) {
        Driver driver = getDriverByUserId(userId);

        DriverLocation location = driverLocationRepository.findByDriverId(driver.getId())
                .map(existing -> {
                    existing.updatePosition(lat, lng);
                    return existing;
                })
                .orElse(new DriverLocation(driver.getId(), lat, lng));

        return driverLocationRepository.save(location);
    }

    @Transactional(readOnly = true)
    public Driver getDriverByUserId(Long userId) {
        return driverRepository.findByUserId(userId)
                .orElseThrow(() -> new DriverNotFound(userId));
    }
}
