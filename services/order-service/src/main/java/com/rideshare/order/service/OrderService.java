package com.rideshare.order.service;

import com.rideshare.commons.dto.order.CancelOrderRequest;
import com.rideshare.commons.dto.order.OrderRequest;
import com.rideshare.order.domain.Order;
import com.rideshare.order.repository.OrderRepository;
import com.rideshare.order.web.exception.OrderCannotBeCancelled;
import com.rideshare.order.web.exception.OrderNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private static final Set<Order.OrderStatus> CANCELLABLE_STATUSES =
            Set.of(Order.OrderStatus.REQUESTED, Order.OrderStatus.ACCEPTED);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(OrderRequest request, Long riderId) {
        Order entity = Order.fromTransferObject(request, riderId);
        return orderRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId, Long callerId, boolean isDriver) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound(orderId));
        if (!isDriver && !order.getRiderId().equals(callerId)) {
            throw new OrderNotFound(orderId);
        }
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> getMyOrders(Long riderId) {
        return orderRepository.findAllByRiderId(riderId);
    }

    @Transactional
    public Order cancelOrder(Long orderId, Long riderId, CancelOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound(orderId));
        if (!order.getRiderId().equals(riderId)) {
            throw new OrderNotFound(orderId);
        }
        if (!CANCELLABLE_STATUSES.contains(order.getStatus())) {
            throw new OrderCannotBeCancelled(orderId, order.getStatus());
        }
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancellationReason(request != null ? request.getReason() : null);
        return orderRepository.save(order);
    }
}
