package cz.rohlik.service.stock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemDto(
        Long id,
        @NotNull(message = "Product cannot be null")
        ProductDto product,
        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be positive")
        Integer quantity) {

}

