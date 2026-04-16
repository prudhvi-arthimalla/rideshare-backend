package com.rideshare.order.web.dto;

import com.rideshare.order.domain.Order;
import com.rideshare.order.domain.Order.OrderStatus;

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

    public static OrderResponse fromOrder(Order order) {
        OrderResponse response = new OrderResponse();
        response.id = order.getId();
        response.riderId = order.getRiderId();
        response.driverId = order.getDriverId();
        response.status = order.getStatus();
        response.pickupLocation = order.getPickupLocation();
        response.dropOffLocation = order.getDropOffLocation();
        response.pickupLat = order.getPickupLat();
        response.pickupLng = order.getPickupLng();
        response.dropOffLat = order.getDropOffLat();
        response.dropOffLng = order.getDropOffLng();
        response.cancellationReason = order.getCancellationReason();
        response.createdAt = order.getCreatedAt();
        response.updatedAt = order.getUpdatedAt();
        return response;
    }

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
}
