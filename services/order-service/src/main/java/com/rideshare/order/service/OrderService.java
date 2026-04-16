package com.rideshare.order.service;

import com.rideshare.order.domain.Order;
import com.rideshare.order.repository.OrderRepository;
import com.rideshare.order.web.dto.OrderRequest;
import com.rideshare.order.web.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    public final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest request) {
        var order = Order.fromTransferObject(request, 1L);
        return order.toTransferObject();
    }
}
