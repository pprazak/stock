package cz.rohlik.service.stock.service.impl;

import cz.rohlik.service.stock.config.StockReservationConfig;
import cz.rohlik.service.stock.domain.Order;
import cz.rohlik.service.stock.domain.OrderItem;
import cz.rohlik.service.stock.domain.OrderStatus;
import cz.rohlik.service.stock.repository.OrderRepository;
import cz.rohlik.service.stock.repository.ProductRepository;
import cz.rohlik.service.stock.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockReservationConfig stockReservationConfig;
    private final Clock clock;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, StockReservationConfig stockReservationConfig, Clock clock) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.stockReservationConfig = stockReservationConfig;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            int updatedRows = productRepository.decreaseStock(item.getProduct().getId(), item.getQuantity());

            if (updatedRows == 0) {
                throw new IllegalStateException("Not enough stock for product: " + item.getProduct().getName());
            }
        }

        order.setStatus(OrderStatus.PENDING);
        order.setReservedUntil(LocalDateTime.now(clock).plusMinutes(stockReservationConfig.getTimeLimitInMinutes()));
        orderRepository.save(order);
        return order;
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Cannot cancel order in status " + order.getStatus());
        }

        for (OrderItem item : order.getOrderItems()) {
            productRepository.releaseStock(item.getProduct().getId(), item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return order;
    }

    @Override
    @Transactional
    public Order setOrderPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be paid for");
        }

        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now(clock));
        order.setReservedUntil(null);
        orderRepository.save(order);
        return order;
    }

    @Override
    @Transactional
    public int expireOrders() {
        LocalDateTime now = LocalDateTime.now(clock);

        List<Order> expiredOrders = orderRepository.findByStatusAndReservedUntilBefore(OrderStatus.PENDING, now);

        if (expiredOrders.isEmpty()) {
            return 0;
        }

        for (Order order : expiredOrders) {
            order.setStatus(OrderStatus.EXPIRED);
            for (OrderItem orderItem : order.getOrderItems()) {
                productRepository.releaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());
            }
        }

        orderRepository.saveAll(expiredOrders);

        return expiredOrders.size();
    }

}
