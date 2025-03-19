package cz.rohlik.service.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemDto(
        Long id,
        @NotNull
        ProductDto product,
        @NotNull
        @Min(1)
        Integer quantity) {

}

