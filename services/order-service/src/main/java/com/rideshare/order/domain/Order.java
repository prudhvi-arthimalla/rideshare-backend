package com.rideshare.order.domain;

import com.rideshare.order.web.dto.OrderRequest;
import com.rideshare.order.web.dto.OrderResponse;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long riderId;

    @Column
    private Long driverId;  // null until a driver accepts

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String dropOffLocation;

    @Column(precision = 9, scale = 6)
    private BigDecimal pickupLat;

    @Column(precision = 9, scale = 6)
    private BigDecimal pickupLng;

    @Column(precision = 9, scale = 6)
    private BigDecimal dropOffLat;

    @Column(precision = 9, scale = 6)
    private BigDecimal dropOffLng;

    @Column
    private String cancellationReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        status = OrderStatus.REQUESTED;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = Instant.now();
    }

    public enum OrderStatus {
        REQUESTED,
        ACCEPTED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    // getters
    public Long getId() { return id; }
    public Long getRiderId() { return riderId; }
    public Long getDriverId() { return driverId; }
    public OrderStatus getStatus() { return status; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDropOffLocation() { return dropOffLocation; }
    public BigDecimal getPickupLat() { return pickupLat; }
    public BigDecimal getPickupLng() { return pickupLng; }
    public BigDecimal getDropOffLat() { return dropOffLat; }
    public BigDecimal getDropOffLng() { return dropOffLng; }
    public String getCancellationReason() { return cancellationReason; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // setters (no setId, no setCreatedAt — immutable)
    public void setRiderId(Long riderId) { this.riderId = riderId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public void setDropOffLocation(String dropOffLocation) { this.dropOffLocation = dropOffLocation; }
    public void setPickupLat(BigDecimal pickupLat) { this.pickupLat = pickupLat; }
    public void setPickupLng(BigDecimal pickupLng) { this.pickupLng = pickupLng; }
    public void setDropOffLat(BigDecimal dropOffLat) { this.dropOffLat = dropOffLat; }
    public void setDropOffLng(BigDecimal dropOffLng) { this.dropOffLng = dropOffLng; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public static Order fromTransferObject(OrderRequest request, Long riderId) {
        Order order = new Order();
        order.setRiderId(riderId);
        order.setPickupLocation(request.getPickupLocation());
        order.setDropOffLocation(request.getDropOffLocation());
        order.setPickupLat(request.getPickupLat());
        order.setPickupLng(request.getPickupLng());
        order.setDropOffLat(request.getDropOffLat());
        order.setDropOffLng(request.getDropOffLng());
        return order;
    }

    public OrderResponse toTransferObject() {
        return OrderResponse.fromOrder(this);
    }
}
