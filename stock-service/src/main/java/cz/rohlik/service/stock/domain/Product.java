package cz.rohlik.service.stock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

import static cz.rohlik.service.stock.domain.Product.SQ_GENERATOR;

@Entity
@Table(name = "product", schema = Schema.STOCK)
@SequenceGenerator(allocationSize = 1, name = SQ_GENERATOR, sequenceName = "sq_product", schema = Schema.STOCK)
public class Product {

    static final String SQ_GENERATOR = "ProductSequenceGenerator";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SQ_GENERATOR)
    private Long id;

    @Version
    private Integer version;

    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer quantityInStock;

    @NotNull
    @Positive
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
