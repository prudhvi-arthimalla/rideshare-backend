package com.rideshare.order.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String messageKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant publishedAt;

    protected OutboxEvent() {}

    public OutboxEvent(String topic, String messageKey, String payload) {
        this.topic = topic;
        this.messageKey = messageKey;
        this.payload = payload;
    }

    @PrePersist
    private void onCreate() {
        id = UUID.randomUUID();
        createdAt = Instant.now();
    }

    public void markPublished() {
        this.publishedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getTopic() { return topic; }
    public String getMessageKey() { return messageKey; }
    public String getPayload() { return payload; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getPublishedAt() { return publishedAt; }
}
