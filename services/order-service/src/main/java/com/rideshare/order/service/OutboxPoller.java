package com.rideshare.order.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideshare.order.domain.OutboxEvent;
import com.rideshare.order.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxPoller {

    private static final Logger log = LoggerFactory.getLogger(OutboxPoller.class);
    private static final int BATCH_SIZE = 50;

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPoller(OutboxEventRepository outboxEventRepository,
                        @Qualifier("orderKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate,
                        ObjectMapper objectMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void poll() {
        List<OutboxEvent> events = outboxEventRepository.findUnpublishedWithLock(BATCH_SIZE);
        if (events.isEmpty()) return;

        for (OutboxEvent event : events) {
            try {
                JsonNode payload = objectMapper.readTree(event.getPayload());
                kafkaTemplate.send(event.getTopic(), event.getMessageKey(), payload).get();
                event.markPublished();
                log.info("Relayed outbox event id={} to topic={}", event.getId(), event.getTopic());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while relaying outbox event id={}", event.getId());
                break;
            } catch (Exception e) {
                log.error("Failed to relay outbox event id={} topic={}", event.getId(), event.getTopic(), e);
            }
        }
    }
}
