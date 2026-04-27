package com.rideshare.commons.kafka.events;

import java.time.Instant;

public class OrderCancelledEvent {

    private Long orderId;
    private Long riderId;
    private Long driverId;
    private String reason;
    private Instant cancelledAt;

    public static OrderCancelledEvent of(Long orderId, Long riderId, Long driverId,
                                         String reason, Instant cancelledAt) {
        OrderCancelledEvent cancelledEvent = new OrderCancelledEvent();
        cancelledEvent.orderId = orderId;
        cancelledEvent.riderId = riderId;
        cancelledEvent.driverId = driverId;
        cancelledEvent.reason = reason;
        cancelledEvent.cancelledAt = cancelledAt;
        return cancelledEvent;
    }

    public Long getOrderId() { return orderId; }
    public Long getRiderId() { return riderId; }
    public Long getDriverId() { return driverId; }
    public String getReason() { return reason; }
    public Instant getCancelledAt() { return cancelledAt; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setRiderId(Long riderId) { this.riderId = riderId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public void setReason(String reason) { this.reason = reason; }
    public void setCancelledAt(Instant cancelledAt) { this.cancelledAt = cancelledAt; }
}
