package com.rideshare.driver.service;

import com.rideshare.commons.kafka.Topics;
import com.rideshare.commons.kafka.events.OrderAcceptedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DriverEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DriverEventPublisher.class);

    private final KafkaTemplate<String, Object> driverKafkaTemplate;

    public DriverEventPublisher(
            @Qualifier("driverKafkaTemplate") KafkaTemplate<String, Object> driverKafkaTemplate) {
        this.driverKafkaTemplate = driverKafkaTemplate;
    }

    public void publishOrderAccepted(Long orderId, Long driverId) {
        OrderAcceptedEvent event = OrderAcceptedEvent.of(orderId, driverId, Instant.now());
        driverKafkaTemplate.send(Topics.ORDER_ACCEPTED, String.valueOf(orderId), event)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Error publishing order accepted event for driver {}", driverId, error);
                    }
                });
        log.info("Published order.accepted event for orderId={}, driverId={}", orderId, driverId);
    }
}
