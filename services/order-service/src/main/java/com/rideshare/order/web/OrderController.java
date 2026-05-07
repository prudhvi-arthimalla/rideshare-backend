package com.rideshare.order.web;

import com.rideshare.commons.dto.order.CancelOrderRequest;
import com.rideshare.commons.dto.order.OrderRequest;
import com.rideshare.commons.dto.order.OrderResponse;
import com.rideshare.order.domain.Order;
import com.rideshare.order.service.OrderService;
import com.rideshare.order.web.examples.RequestExample;
import com.rideshare.order.web.examples.ResponseExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Place a new order",
            description = "Create a new ride request. Requires RIDER role.",
            responses = {
                    @ApiResponse(
                            description = "Order successfully created.",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponse.class),
                                    examples = @ExampleObject(ResponseExample.ORDER_RESPONSE)
                            )
                    ),
                    @ApiResponse(
                            description = "Invalid request body.",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    )
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid
                                     @RequestBody
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                             description = "Order creation payload.",
                                             required = true,
                                             content = @Content(
                                                     mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                     schema = @Schema(implementation = OrderRequest.class),
                                                     examples = @ExampleObject(RequestExample.CREATE_ORDER_REQUEST)
                                             )
                                     ) OrderRequest request,
                                     Authentication authentication) {
        Long riderId = (Long) authentication.getDetails();
        log.info("Received request to create order from user {}", riderId);
        return orderService.createOrder(request, riderId).toTransferObject();
    }

    @Operation(
            summary = "Get all orders for the current rider",
            description = "Returns a list of all orders placed by the authenticated rider.",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved orders.",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponse.class),
                                    examples = @ExampleObject(ResponseExample.ORDER_RESPONSE)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    )
            }
    )
    @GetMapping(value = "/my-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        Long riderId = (Long) authentication.getDetails();
        log.info("Received request to fetch all orders for user {}", riderId);
        return orderService.getMyOrders(riderId).stream()
                .map(Order::toTransferObject)
                .toList();
    }

    @Operation(
            summary = "Get order details by id",
            description = "Fetch a specific order. Riders can only view their own orders; drivers can view any order.",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved order.",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponse.class),
                                    examples = @ExampleObject(ResponseExample.ORDER_RESPONSE)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    ),
                    @ApiResponse(
                            description = "Order not found.",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    )
            }
    )
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse getOrder(@PathVariable Long id, Authentication authentication) {
        Long callerId = (Long) authentication.getDetails();
        boolean isDriver = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DRIVER"));
        log.info("Received request to fetch order details by user {}", callerId);
        return orderService.getOrder(id, callerId, isDriver).toTransferObject();
    }

    @Operation(
            summary = "Cancel an order",
            description = "Cancel an order by id. Only the rider who placed the order can cancel it. Order must be in REQUESTED or ACCEPTED status.",
            responses = {
                    @ApiResponse(
                            description = "Order successfully cancelled.",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = OrderResponse.class),
                                    examples = @ExampleObject(ResponseExample.ORDER_CANCELLED_RESPONSE)
                            )
                    ),
                    @ApiResponse(
                            description = "Order cannot be cancelled in its current status.",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized.",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    ),
                    @ApiResponse(
                            description = "Order not found.",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject()
                            )
                    )
            }
    )
    @PutMapping(value = "/{id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse cancelOrder(@PathVariable Long id,
                                     @RequestBody(required = false)
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                             description = "Optional cancellation reason.",
                                             required = false,
                                             content = @Content(
                                                     mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                     schema = @Schema(implementation = CancelOrderRequest.class),
                                                     examples = @ExampleObject(RequestExample.CANCEL_ORDER_REQUEST)
                                             )
                                     ) CancelOrderRequest request,
                                     Authentication authentication) {
        Long riderId = (Long) authentication.getDetails();
        log.info("Received request to cancel order for user {}", riderId);
        return orderService.cancelOrder(id, riderId, request).toTransferObject();
    }
}
