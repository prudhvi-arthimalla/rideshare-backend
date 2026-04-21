package com.rideshare.order.service;

import com.rideshare.commons.dto.order.OrderRequest;
import com.rideshare.order.domain.Order;
import com.rideshare.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderRequest request, Long riderId) {
        Order entity = Order.fromTransferObject(request, riderId);
        return orderRepository.save(entity);
    }
}
