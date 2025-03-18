package cz.rohlik.service.stock.repository;

import cz.rohlik.service.stock.domain.Order;
import cz.rohlik.service.stock.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderItemsProductIdAndStatusNotIn(Long productId, Set<OrderStatus> statuses);

    List<Order> findByStatusAndReservedUntilBefore(OrderStatus status, LocalDateTime dateTime);
}
