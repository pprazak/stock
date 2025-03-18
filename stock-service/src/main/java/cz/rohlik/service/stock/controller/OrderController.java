package cz.rohlik.service.stock.controller;

import cz.rohlik.service.stock.dto.CreateOrderRequest;
import cz.rohlik.service.stock.dto.OrderDto;
import cz.rohlik.service.stock.endpoint.OrderResource;
import cz.rohlik.service.stock.mapper.OrderMapper;
import cz.rohlik.service.stock.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderResource {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @Override
    @Operation(summary = "Creates a new order", description = "Creates a new order.")
    @ApiResponse(responseCode = "201", description = "Order successfully created.")
    @ApiResponse(responseCode = "400", description = "Invalid order parameters.")
    public ResponseEntity<OrderDto> createOrder(
            @Parameter(description = "Order details that are used to create a new order.") @Valid CreateOrderRequest order) {
        var createdOrder = orderService.createOrder(order);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.map(createdOrder));
    }

    @Override
    @Operation(summary = "Cancels an order", description = "Cancels an order.")
    @ApiResponse(responseCode = "200", description = "Order successfully canceled.")
    @ApiResponse(responseCode = "404", description = "Order not found.")
    public ResponseEntity<OrderDto> cancelOrder(
            @Parameter(description = "ID of the order to be canceled.") Long id) {
        var cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(orderMapper.map(cancelledOrder));
    }

    @Override
    @Operation(summary = "Marks an order as paid", description = "Marks an order as paid.")
    @ApiResponse(responseCode = "200", description = "Order marked as paid.")
    @ApiResponse(responseCode = "404", description = "Order not found.")
    public ResponseEntity<OrderDto> payOrder(
            @Parameter(description = "ID of the order to be marked as paid.") Long id) {
        var paidOrder = orderService.setOrderPaid(id);
        return ResponseEntity.ok(orderMapper.map(paidOrder));
    }
}
