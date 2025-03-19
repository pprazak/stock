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
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cz.rohlik.service.stock.domain.Order.SQ_GENERATOR;

@Entity
@Table(name = "\"order\"", schema = Schema.STOCK)
@SequenceGenerator(allocationSize = 1, name = SQ_GENERATOR, sequenceName = "sq_order", schema = Schema.STOCK)
public class Order {

    static final String SQ_GENERATOR = "OrderSequenceGenerator";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SQ_GENERATOR)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private LocalDateTime reservedUntil;

    private LocalDateTime paidAt;

    public Long getId() {
        return id;
    }

    public Order setId(final Long id) {
        this.id = id;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Order setStatus(final OrderStatus status) {
        this.status = status;
        return this;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Order setOrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public Order addOrderItem(final OrderItem orderItem) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public Order setReservedUntil(final LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
        return this;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public Order setPaidAt(final LocalDateTime paidAt) {
        this.paidAt = paidAt;
        return this;
    }
}
