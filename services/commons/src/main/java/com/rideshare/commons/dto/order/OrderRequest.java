package com.rideshare.commons.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrderRequest {

    @NotBlank
    private String pickupLocation;

    @NotBlank
    private String dropOffLocation;

    @NotNull
    private BigDecimal pickupLat;

    @NotNull
    private BigDecimal pickupLng;

    @NotNull
    private BigDecimal dropOffLat;

    @NotNull
    private BigDecimal dropOffLng;

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropOffLocation() { return dropOffLocation; }
    public void setDropOffLocation(String dropOffLocation) { this.dropOffLocation = dropOffLocation; }

    public BigDecimal getPickupLat() { return pickupLat; }
    public void setPickupLat(BigDecimal pickupLat) { this.pickupLat = pickupLat; }

    public BigDecimal getPickupLng() { return pickupLng; }
    public void setPickupLng(BigDecimal pickupLng) { this.pickupLng = pickupLng; }

    public BigDecimal getDropOffLat() { return dropOffLat; }
    public void setDropOffLat(BigDecimal dropOffLat) { this.dropOffLat = dropOffLat; }

    public BigDecimal getDropOffLng() { return dropOffLng; }
    public void setDropOffLng(BigDecimal dropOffLng) { this.dropOffLng = dropOffLng; }
}
