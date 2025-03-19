package cz.rohlik.service.stock.service.impl;

import cz.rohlik.service.stock.domain.Order;
import cz.rohlik.service.stock.domain.OrderItem;
import cz.rohlik.service.stock.domain.OrderStatus;
import cz.rohlik.service.stock.domain.Product;
import cz.rohlik.service.stock.dto.CreateOrderRequest;
import cz.rohlik.service.stock.dto.OrderItemRequest;
import cz.rohlik.service.stock.exception.InsufficientStockException;
import cz.rohlik.service.stock.repository.OrderRepository;
import cz.rohlik.service.stock.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private final Clock fixedClock = Clock.fixed(Instant.parse("2025-03-19T12:00:00Z"), ZoneId.systemDefault());
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;

    private OrderServiceImpl orderService;

    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        product = new Product().setId(1L).setName("Test Product").setQuantityInStock(10);
        order = new Order().setId(1L).setStatus(OrderStatus.PENDING);

        orderService = new OrderServiceImpl(orderRepository, productRepository, fixedClock, 30);
    }

    @Test
    void createOrder_shouldSaveAndReturnOrder() {
        var itemRequest = new OrderItemRequest(1L, 2);
        var orderRequest = new CreateOrderRequest(List.of(itemRequest));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.decreaseStock(1L, 2)).thenReturn(1);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        var createdOrder = orderService.createOrder(orderRequest);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_shouldThrowException_whenProductNotFound() {
        var itemRequest = new OrderItemRequest(1L, 2);
        var orderRequest = new CreateOrderRequest(List.of(itemRequest));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderRequest));
    }

    @Test
    void createOrder_shouldThrowException_whenInsufficientStock() {
        var itemRequest = new OrderItemRequest(1L, 20);
        var orderRequest = new CreateOrderRequest(List.of(itemRequest));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.decreaseStock(1L, 20)).thenReturn(0);

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(orderRequest));
    }

    @Test
    void cancelOrder_shouldCancelOrderAndReleaseStock() {
        var orderItem = new OrderItem().setProduct(product).setQuantity(2);
        order.setOrderItems(List.of(orderItem));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        var canceledOrder = orderService.cancelOrder(1L);

        assertThat(canceledOrder.getStatus()).isEqualTo(OrderStatus.CANCELED);
        verify(productRepository, times(1)).releaseStock(1L, 2);
    }

    @Test
    void cancelOrder_shouldThrowException_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void setOrderPaid_shouldSetStatusToPaid() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        var paidOrder = orderService.setOrderPaid(1L);

        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void setOrderPaid_shouldThrowException_whenOrderNotPending() {
        order.setStatus(OrderStatus.CANCELED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> orderService.setOrderPaid(1L));
    }

    @Test
    void expireOrders_shouldExpireOrdersAndReleaseStock() {
        var orderItem = new OrderItem().setProduct(product).setQuantity(2);
        order.setOrderItems(List.of(orderItem));
        LocalDateTime pastTime = LocalDateTime.now(fixedClock).minusMinutes(31);
        order.setReservedUntil(pastTime);

        when(orderRepository.findByStatusAndReservedUntilBefore(eq(OrderStatus.PENDING), any(LocalDateTime.class))).thenReturn(List.of(order));

        int expiredCount = orderService.expireOrders();

        assertThat(expiredCount).isEqualTo(1);
        verify(productRepository, times(1)).releaseStock(1L, 2);
    }
}
