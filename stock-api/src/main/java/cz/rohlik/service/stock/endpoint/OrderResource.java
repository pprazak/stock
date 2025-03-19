package cz.rohlik.service.stock.endpoint;

import cz.rohlik.service.stock.dto.CreateOrderRequest;
import cz.rohlik.service.stock.dto.OrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Orders", description = "Operations related to orders")
@RequestMapping("/orders")
public interface OrderResource {

    @PostMapping
    @Operation(summary = "Creates a new order", description = "Creates a new order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid order parameters.")
    })
    ResponseEntity<OrderDto> createOrder(
            @Parameter(description = "Order details that are used to create a new order.") @RequestBody @Valid CreateOrderRequest order);

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancels an order", description = "Cancels an order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully canceled."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    ResponseEntity<OrderDto> cancelOrder(
            @Parameter(description = "ID of the order to be canceled.") @PathVariable Long id);

    @PatchMapping("/{id}/pay")
    @Operation(summary = "Marks an order as paid", description = "Marks an order as paid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order marked as paid."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    ResponseEntity<OrderDto> payOrder(
            @Parameter(description = "ID of the order to be marked as paid.") @PathVariable Long id);
}
