package cz.rohlik.service.stock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductDto(Long id,
                         @NotNull(message = "Name cannot be null")
                         @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
                         String name,
                         @NotNull(message = "Quantity in stock cannot be null")
                         @Positive(message = "Quantity in stock must be positive")
                         Integer quantityInStock,
                         @NotNull(message = "Price cannot be null")
                         @Positive(message = "Price must be positive")
                         BigDecimal price) {

}
