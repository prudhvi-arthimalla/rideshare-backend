package com.rideshare.commons.dto.order;

import java.math.BigDecimal;
import java.time.Instant;

public class OrderResponse {

    private Long id;
    private Long riderId;
    private Long driverId;
    private OrderStatus status;
    private String pickupLocation;
    private String dropOffLocation;
    private BigDecimal pickupLat;
    private BigDecimal pickupLng;
    private BigDecimal dropOffLat;
    private BigDecimal dropOffLng;
    private String cancellationReason;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRiderId() { return riderId; }
    public void setRiderId(Long riderId) { this.riderId = riderId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropOffLocation() { return dropOffLocation; }
    public void setDropOffLocation(String dropOffLocation) { this.dropOffLocation = dropOffLocation; }

    public BigDecimal getPickupLat() { return pickupLat; }
    public void setPickupLat(BigDecimal pickupLat) { this.pickupLat = pickupLat; }

    public BigDecimal getPickupLng() { return pickupLng; }
    public void setPickupLng(BigDecimal pickupLng) { this.pickupLng = pickupLng; }

    public BigDecimal getDropOffLat() { return dropOffLat; }
    public void setDropOffLat(BigDecimal dropOffLat) { this.dropOffLat = dropOffLat; }

    public BigDecimal getDropOffLng() { return dropOffLng; }
    public void setDropOffLng(BigDecimal dropOffLng) { this.dropOffLng = dropOffLng; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
