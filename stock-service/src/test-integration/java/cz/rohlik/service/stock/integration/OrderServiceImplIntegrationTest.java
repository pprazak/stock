package cz.rohlik.service.stock.integration;

import cz.rohlik.service.stock.domain.Order;
import cz.rohlik.service.stock.domain.OrderItem;
import cz.rohlik.service.stock.domain.OrderStatus;
import cz.rohlik.service.stock.domain.Product;
import cz.rohlik.service.stock.repository.OrderRepository;
import cz.rohlik.service.stock.repository.ProductRepository;
import cz.rohlik.service.stock.service.impl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceImplIntegrationTest {

    private final Clock fixedClock = Clock.fixed(Instant.parse("2025-03-19T12:00:00Z"), ZoneId.systemDefault());
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        orderRepository.deleteAll();

        var product = new Product()
                .setName("Test Product")
                .setQuantityInStock(10)
                .setPrice(new BigDecimal("100.0"));
        productRepository.save(product);

        var order = new Order()
                .setStatus(OrderStatus.PENDING)
                .setReservedUntil(LocalDateTime.now(fixedClock).plusMinutes(30))
                .addOrderItem(new OrderItem().setProduct(product).setQuantity(1));
        orderRepository.save(order);
    }

    @Test
    void expireOrders_shouldExpirePendingOrders() {
        int expiredCount = orderService.expireOrders();
        assertThat(expiredCount).isEqualTo(1);

        var expiredOrders = orderRepository.findAll();
        assertThat(expiredOrders).hasSize(1);
        assertThat(expiredOrders.getFirst().getStatus()).isEqualTo(OrderStatus.EXPIRED);
    }

    @Test
    void setOrderPaid_shouldUpdateOrderStatusToPaid() {
        var order = orderRepository.findAll().getFirst();
        var paidOrder = orderService.setOrderPaid(order.getId());

        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void cancelOrder_shouldUpdateOrderStatusToCanceled() {
        var order = orderRepository.findAll().getFirst();
        var canceledOrder = orderService.cancelOrder(order.getId());

        assertThat(canceledOrder.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void cancelOrder_shouldThrowException_whenOrderNotFound() {
        assertThrows(EntityNotFoundException.class, () -> orderService.cancelOrder(999L));
    }
}
