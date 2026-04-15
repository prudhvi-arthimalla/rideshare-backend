package com.rideshare.order.service;

import com.rideshare.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
