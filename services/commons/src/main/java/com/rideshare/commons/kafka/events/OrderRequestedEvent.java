package com.rideshare.commons.kafka.events;

import java.math.BigDecimal;
import java.time.Instant;

public class OrderRequestedEvent {

    private Long orderId;
    private Long riderId;
    private String pickupLocation;
    private String dropOffLocation;
    private BigDecimal pickupLat;
    private BigDecimal pickupLng;
    private BigDecimal dropOffLat;
    private BigDecimal dropOffLng;
    private Instant requestedAt;

    public static OrderRequestedEvent of(Long orderId, Long riderId,
                                         String pickupLocation, String dropOffLocation,
                                         BigDecimal pickupLat, BigDecimal pickupLng,
                                         BigDecimal dropOffLat, BigDecimal dropOffLng,
                                         Instant requestedAt) {
        OrderRequestedEvent requestedEvent = new OrderRequestedEvent();
        requestedEvent.orderId = orderId;
        requestedEvent.riderId = riderId;
        requestedEvent.pickupLocation = pickupLocation;
        requestedEvent.dropOffLocation = dropOffLocation;
        requestedEvent.pickupLat = pickupLat;
        requestedEvent.pickupLng = pickupLng;
        requestedEvent.dropOffLat = dropOffLat;
        requestedEvent.dropOffLng = dropOffLng;
        requestedEvent.requestedAt = requestedAt;
        return requestedEvent;
    }

    public Long getOrderId() { return orderId; }
    public Long getRiderId() { return riderId; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDropOffLocation() { return dropOffLocation; }
    public BigDecimal getPickupLat() { return pickupLat; }
    public BigDecimal getPickupLng() { return pickupLng; }
    public BigDecimal getDropOffLat() { return dropOffLat; }
    public BigDecimal getDropOffLng() { return dropOffLng; }
    public Instant getRequestedAt() { return requestedAt; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setRiderId(Long riderId) { this.riderId = riderId; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public void setDropOffLocation(String dropOffLocation) { this.dropOffLocation = dropOffLocation; }
    public void setPickupLat(BigDecimal pickupLat) { this.pickupLat = pickupLat; }
    public void setPickupLng(BigDecimal pickupLng) { this.pickupLng = pickupLng; }
    public void setDropOffLat(BigDecimal dropOffLat) { this.dropOffLat = dropOffLat; }
    public void setDropOffLng(BigDecimal dropOffLng) { this.dropOffLng = dropOffLng; }
    public void setRequestedAt(Instant requestedAt) { this.requestedAt = requestedAt; }
}
