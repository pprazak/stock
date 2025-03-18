package cz.rohlik.service.stock.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(@Size(min = 1)
                                 List<OrderItemRequest> orderItems) {

}
