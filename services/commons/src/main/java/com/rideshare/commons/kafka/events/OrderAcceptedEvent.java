package com.rideshare.commons.kafka.events;

import java.time.Instant;

public class OrderAcceptedEvent {

    private Long orderId;
    private Long driverId;
    private Instant acceptedAt;

    public static OrderAcceptedEvent of(Long orderId, Long driverId, Instant acceptedAt) {
        OrderAcceptedEvent event = new OrderAcceptedEvent();
        event.orderId = orderId;
        event.driverId = driverId;
        event.acceptedAt = acceptedAt;
        return event;
    }

    public Long getOrderId() { return orderId; }
    public Long getDriverId() { return driverId; }
    public Instant getAcceptedAt() { return acceptedAt; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public void setAcceptedAt(Instant acceptedAt) { this.acceptedAt = acceptedAt; }
}
