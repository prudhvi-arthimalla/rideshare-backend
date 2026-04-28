package com.rideshare.order.web.exception;

import com.rideshare.commons.dto.order.OrderStatus;

public class OrderCannotBeCancelled extends RuntimeException {

    public OrderCannotBeCancelled(Long id, OrderStatus currentStatus) {
        super("Order " + id + " cannot be cancelled in status: " + currentStatus);
    }
}
