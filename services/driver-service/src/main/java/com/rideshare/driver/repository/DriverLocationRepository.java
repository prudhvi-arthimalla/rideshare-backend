package com.rideshare.driver.repository;

import com.rideshare.driver.document.DriverLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverLocationRepository extends MongoRepository<DriverLocation, String> {

    Optional<DriverLocation> findByDriverId(Long driverId);
}
