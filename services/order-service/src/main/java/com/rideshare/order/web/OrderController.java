package com.rideshare.order.web;

import com.rideshare.commons.dto.order.CancelOrderRequest;
import com.rideshare.commons.dto.order.OrderRequest;
import com.rideshare.commons.dto.order.OrderResponse;
import com.rideshare.order.domain.Order;
import com.rideshare.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Order creation and management")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Place a new order")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request,
                                     Authentication authentication) {
        Long riderId = (Long) authentication.getDetails();
        return orderService.createOrder(request, riderId).toTransferObject();
    }

    @Operation(summary = "Get all orders for the current rider")
    @GetMapping(value = "/my-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        Long riderId = (Long) authentication.getDetails();
        return orderService.getMyOrders(riderId).stream()
                .map(Order::toTransferObject)
                .toList();
    }

    @Operation(summary = "Get order details by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse getOrder(@PathVariable Long id, Authentication authentication) {
        Long callerId = (Long) authentication.getDetails();
        boolean isDriver = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DRIVER"));
        return orderService.getOrder(id, callerId, isDriver).toTransferObject();
    }

    @Operation(summary = "Cancel an order")
    @PutMapping(value = "/{id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse cancelOrder(@PathVariable Long id,
                                     @RequestBody(required = false) CancelOrderRequest request,
                                     Authentication authentication) {
        Long riderId = (Long) authentication.getDetails();
        return orderService.cancelOrder(id, riderId, request).toTransferObject();
    }
}
