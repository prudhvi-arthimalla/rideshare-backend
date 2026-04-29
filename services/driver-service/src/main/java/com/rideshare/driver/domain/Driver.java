package com.rideshare.driver.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) status = DriverStatus.OFFLINE;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = Instant.now();
    }

    public enum DriverStatus {
        AVAILABLE, BUSY, OFFLINE
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public DriverStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setStatus(DriverStatus status) { this.status = status; }
}
