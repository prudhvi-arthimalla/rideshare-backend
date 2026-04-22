package com.rideshare.order.web.exception;

import com.rideshare.order.domain.Order;

public class OrderCannotBeCancelled extends RuntimeException {

    public OrderCannotBeCancelled(Long id, Order.OrderStatus currentStatus) {
        super("Order " + id + " cannot be cancelled in status: " + currentStatus);
    }
}
