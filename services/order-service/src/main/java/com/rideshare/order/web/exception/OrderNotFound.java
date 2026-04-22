package com.rideshare.order.web.exception;

public class OrderNotFound extends RuntimeException {

    public OrderNotFound(Long id) {
        super("Order not found: " + id);
    }
}
