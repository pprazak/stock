package cz.rohlik.service.stock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
    @Min(0)
    private Integer quantityInStock;

    @NotNull
    @Positive
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public Product setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public Product setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
