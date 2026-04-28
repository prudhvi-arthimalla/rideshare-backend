package com.rideshare.order.service;

import com.rideshare.commons.dto.order.CancelOrderRequest;
import com.rideshare.commons.dto.order.OrderRequest;
import com.rideshare.commons.dto.order.OrderStatus;
import com.rideshare.commons.kafka.events.OrderCancelledEvent;
import com.rideshare.commons.kafka.events.OrderRequestedEvent;
import com.rideshare.order.domain.Order;
import com.rideshare.order.repository.OrderRepository;
import com.rideshare.order.web.exception.OrderCannotBeCancelled;
import com.rideshare.order.web.exception.OrderNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private static final Set<OrderStatus> CANCELLABLE_STATUSES =
            Set.of(OrderStatus.REQUESTED, OrderStatus.ACCEPTED);

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, EventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order createOrder(OrderRequest request, Long riderId) {
        Order savedOrder = orderRepository.save(Order.fromTransferObject(request, riderId));
        log.info("Order created for user {}, orderId: {}", riderId, savedOrder.getId());
        eventPublisher.publishOrderRequested(OrderRequestedEvent.of(
                savedOrder.getId(),
                savedOrder.getRiderId(),
                savedOrder.getPickupLocation(),
                savedOrder.getDropOffLocation(),
                savedOrder.getPickupLat(),
                savedOrder.getPickupLng(),
                savedOrder.getDropOffLat(),
                savedOrder.getDropOffLng(),
                savedOrder.getCreatedAt()
        ));
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId, Long callerId, boolean isDriver) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound(orderId));
        if (!isDriver && !order.getRiderId().equals(callerId)) {
            log.warn("User requested to fetch order {}, but does not have correct permission", orderId);
            throw new OrderNotFound(orderId);
        }
        log.info("Fetched order {}", orderId);
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
            log.warn("User requested to cancel order {}, but does not have correct permission", orderId);
            throw new OrderNotFound(orderId);
        }
        if (!CANCELLABLE_STATUSES.contains(order.getStatus())) {
            log.warn("User requested to cancel order but order is not cancellable {}", order.getStatus());
            throw new OrderCannotBeCancelled(orderId, order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(request != null ? request.getReason() : null);
        Order savedOrder = orderRepository.save(order);
        log.info("Order cancelled for user {}, orderId: {}", riderId, savedOrder.getId());
        eventPublisher.publishOrderCancelled(OrderCancelledEvent.of(
                savedOrder.getId(),
                savedOrder.getRiderId(),
                savedOrder.getDriverId(),
                savedOrder.getCancellationReason(),
                savedOrder.getUpdatedAt()
        ));
        return savedOrder;
    }
}
