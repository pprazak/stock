package cz.rohlik.service.stock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static cz.rohlik.service.stock.domain.OrderItem.SQ_GENERATOR;

@Entity
@Table(name = "order_item", schema = Schema.STOCK)
@SequenceGenerator(allocationSize = 1, name = SQ_GENERATOR, sequenceName = "sq_order_item", schema = Schema.STOCK)
public class OrderItem {

    static final String SQ_GENERATOR = "OrderItemSequenceGenerator";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SQ_GENERATOR)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Positive
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
