package cz.rohlik.service.stock.controller;

import cz.rohlik.service.stock.dto.CreateOrderRequest;
import cz.rohlik.service.stock.dto.OrderDto;
import cz.rohlik.service.stock.endpoint.OrderResource;
import cz.rohlik.service.stock.mapper.OrderMapper;
import cz.rohlik.service.stock.service.OrderService;
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
    public ResponseEntity<OrderDto> createOrder(CreateOrderRequest order) {
        var createdOrder = orderService.createOrder(order);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.map(createdOrder));
    }

    @Override
    public ResponseEntity<OrderDto> cancelOrder(Long id) {
        var canceledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(orderMapper.map(canceledOrder));
    }

    @Override
    public ResponseEntity<OrderDto> payOrder(Long id) {
        var paidOrder = orderService.setOrderPaid(id);
        return ResponseEntity.ok(orderMapper.map(paidOrder));
    }
}
