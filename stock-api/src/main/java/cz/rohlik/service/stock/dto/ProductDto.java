package cz.rohlik.service.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductDto(Long id,
                         @NotBlank
                         String name,
                         @Min(0)
                         Integer quantityInStock,
                         @Positive
                         BigDecimal price) {

}
