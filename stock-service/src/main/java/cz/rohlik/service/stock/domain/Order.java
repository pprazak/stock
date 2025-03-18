package cz.rohlik.service.stock.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cz.rohlik.service.stock.domain.Order.SQ_GENERATOR;

@Entity
//@Table(name = "order", schema = Schema.STOCK)
@Table(name = "\"order\"", schema = Schema.STOCK)
@SequenceGenerator(allocationSize = 1, name = SQ_GENERATOR, sequenceName = "sq_order", schema = Schema.STOCK)
public class Order {

    static final String SQ_GENERATOR = "OrderSequenceGenerator";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SQ_GENERATOR)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private LocalDateTime reservedUntil;

    private LocalDateTime paidAt;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(final OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(final OrderItem orderItem) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(orderItem);
    }

    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(final LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(final LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
