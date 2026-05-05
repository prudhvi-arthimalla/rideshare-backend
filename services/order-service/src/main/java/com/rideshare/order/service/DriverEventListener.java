package com.rideshare.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideshare.commons.kafka.Topics;
import com.rideshare.commons.kafka.events.OrderAcceptedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DriverEventListener {

    private static final Logger log = LoggerFactory.getLogger(DriverEventListener.class);

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public DriverEventListener(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = Topics.ORDER_ACCEPTED)
    public void onOrderAccepted(String message) {
        OrderAcceptedEvent event;
        try {
            event = objectMapper.readValue(message, OrderAcceptedEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize order.accepted message: {}", message, e);
            return;
        }

        log.info("Received order.accepted event for orderId={}, driverId={}", event.getOrderId(), event.getDriverId());
        orderService.acceptOrder(event.getOrderId(), event.getDriverId());
    }
}
