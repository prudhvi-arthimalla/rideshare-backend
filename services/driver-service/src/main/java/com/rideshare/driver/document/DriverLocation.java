package com.rideshare.driver.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "driver_locations")
public class DriverLocation {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long driverId;

    private double lat;
    private double lng;
    private Instant updatedAt;

    public DriverLocation(Long driverId, double lat, double lng) {
        this.driverId = driverId;
        this.lat = lat;
        this.lng = lng;
        this.updatedAt = Instant.now();
    }

    public void updatePosition(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public Long getDriverId() { return driverId; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public Instant getUpdatedAt() { return updatedAt; }
}
