package com.rideshare.order.converter;

import com.rideshare.commons.dto.order.OrderResponse;
import com.rideshare.commons.dto.order.OrderStatus;
import com.rideshare.order.domain.Order;

public class EntityToTransferObjectConverter {

    public static OrderResponse convert(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setRiderId(order.getRiderId());
        response.setDriverId(order.getDriverId());
        response.setStatus(OrderStatus.valueOf(order.getStatus().name()));
        response.setPickupLocation(order.getPickupLocation());
        response.setDropOffLocation(order.getDropOffLocation());
        response.setPickupLat(order.getPickupLat());
        response.setPickupLng(order.getPickupLng());
        response.setDropOffLat(order.getDropOffLat());
        response.setDropOffLng(order.getDropOffLng());
        response.setCancellationReason(order.getCancellationReason());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }

    private EntityToTransferObjectConverter() {}
}
