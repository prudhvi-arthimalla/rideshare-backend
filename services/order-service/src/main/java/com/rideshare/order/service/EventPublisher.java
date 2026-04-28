package com.rideshare.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideshare.commons.kafka.Topics;
import com.rideshare.commons.kafka.events.OrderCancelledEvent;
import com.rideshare.commons.kafka.events.OrderRequestedEvent;
import com.rideshare.order.domain.OutboxEvent;
import com.rideshare.order.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    public EventPublisher(OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    public void publishOrderRequested(OrderRequestedEvent event) {
        log.info("Received OrderRequested event {}", event);
        outboxEventRepository.save(new OutboxEvent(
                Topics.ORDER_REQUESTED,
                String.valueOf(event.getOrderId()),
                serialize(event)
        ));
    }

    public void publishOrderCancelled(OrderCancelledEvent event) {
        log.info("Received OrderCancelled event {}", event);
        outboxEventRepository.save(new OutboxEvent(
                Topics.ORDER_CANCELLED,
                String.valueOf(event.getOrderId()),
                serialize(event)
        ));
    }

    private String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Error serializing event {}", event, e);
            throw new RuntimeException("Failed to serialize event: " + event.getClass().getSimpleName(), e);
        }
    }
}
