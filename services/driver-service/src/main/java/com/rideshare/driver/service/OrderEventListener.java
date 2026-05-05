package com.rideshare.driver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideshare.commons.kafka.Topics;
import com.rideshare.commons.kafka.events.OrderRequestedEvent;
import com.rideshare.driver.domain.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final DriverService driverService;
    private final DriverEventPublisher driverEventPublisher;
    private final ObjectMapper objectMapper;

    public OrderEventListener(DriverService driverService,
                              DriverEventPublisher driverEventPublisher,
                              ObjectMapper objectMapper) {
        this.driverService = driverService;
        this.driverEventPublisher = driverEventPublisher;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = Topics.ORDER_REQUESTED)
    public void onOrderRequested(String message) {
        OrderRequestedEvent event;
        try {
            event = objectMapper.readValue(message, OrderRequestedEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize order.requested message: {}", message, e);
            return;
        }

        log.info("Received order.requested event for orderId={}", event.getOrderId());

        Optional<Driver> driver = driverService.getNextAvailableDriver();
        if (driver.isEmpty()) {
            log.warn("No available driver found for orderId={}", event.getOrderId());
            return;
        }

        driverEventPublisher.publishOrderAccepted(event.getOrderId(), driver.get().getId());
    }
}
