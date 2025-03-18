package cz.rohlik.service.stock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(Long id,
                       @NotNull(message = "Status cannot be null")
                       @Size(min = 1, max = 50, message = "Status must be between 1 and 50 characters")
                       String status,
                       @NotNull(message = "Order items cannot be null")
                       @Size(min = 1, message = "Order must have at least one item")
                       List<OrderItemDto> orderItems,
                       LocalDateTime reservedUntil,
                       LocalDateTime paidAt) {

}
