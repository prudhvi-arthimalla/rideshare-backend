package com.rideshare.commons.dto.driver;

import java.time.Instant;

public class DriverResponse {

    private Long id;
    private Long userId;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
