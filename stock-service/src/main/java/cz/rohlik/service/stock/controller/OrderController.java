package cz.rohlik.service.stock.controller;

import cz.rohlik.service.stock.dto.OrderDto;
import cz.rohlik.service.stock.endpoint.OrderResource;
import cz.rohlik.service.stock.mapper.OrderMapper;
import cz.rohlik.service.stock.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderResource {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @Override
    @Operation(summary = "Vytvoří nový objednávku", description = "Tato metoda vytvoří novou objednávku na základě předaného objektu OrderDto.")
    @ApiResponse(responseCode = "201", description = "Objednávka úspěšně vytvořena.")
    @ApiResponse(responseCode = "400", description = "Chybné parametry objednávky.")
    public ResponseEntity<OrderDto> createOrder(
            @Parameter(description = "Objekt OrderDto, který obsahuje data pro vytvoření objednávky.") OrderDto orderDTO) {
        var order = orderMapper.map(orderDTO);
        var createdOrder = orderService.createOrder(order);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.map(createdOrder));
    }

    @Override
    @Operation(summary = "Zruší objednávku", description = "Tato metoda zruší objednávku podle jejího ID.")
    @ApiResponse(responseCode = "200", description = "Objednávka byla úspěšně zrušena.")
    @ApiResponse(responseCode = "404", description = "Objednávka nenalezena.")
    public ResponseEntity<OrderDto> cancelOrder(
            @Parameter(description = "ID objednávky, která má být zrušena.") Long id) {
        var cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(orderMapper.map(cancelledOrder));
    }

    @Override
    @Operation(summary = "Označí objednávku jako zaplacenou", description = "Tato metoda označí objednávku jako zaplacenou podle jejího ID.")
    @ApiResponse(responseCode = "200", description = "Objednávka byla označena jako zaplacená.")
    @ApiResponse(responseCode = "404", description = "Objednávka nenalezena.")
    public ResponseEntity<OrderDto> payOrder(
            @Parameter(description = "ID objednávky, která má být označena jako zaplacená.") Long id) {
        var paidOrder = orderService.setOrderPaid(id);
        return ResponseEntity.ok(orderMapper.map(paidOrder));
    }
}
