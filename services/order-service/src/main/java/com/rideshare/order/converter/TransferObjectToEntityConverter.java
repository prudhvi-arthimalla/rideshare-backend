package com.rideshare.order.converter;

import com.rideshare.commons.dto.order.OrderRequest;
import com.rideshare.order.domain.Order;

public class TransferObjectToEntityConverter {

    public static Order convert(OrderRequest dto, Long riderId) {
        Order order = new Order();
        order.setRiderId(riderId);
        order.setPickupLocation(dto.getPickupLocation());
        order.setDropOffLocation(dto.getDropOffLocation());
        order.setPickupLat(dto.getPickupLat());
        order.setPickupLng(dto.getPickupLng());
        order.setDropOffLat(dto.getDropOffLat());
        order.setDropOffLng(dto.getDropOffLng());
        return order;
    }

    private TransferObjectToEntityConverter() {}
}
