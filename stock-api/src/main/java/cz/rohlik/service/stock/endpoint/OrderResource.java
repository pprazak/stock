package cz.rohlik.service.stock.endpoint;

import cz.rohlik.service.stock.dto.CreateOrderRequest;
import cz.rohlik.service.stock.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/orders")
public interface OrderResource {

    @PostMapping
    ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest order);

    @PatchMapping("/{id}/cancel")
    ResponseEntity<OrderDto> cancelOrder(@PathVariable Long id);

    @PatchMapping("/{id}/pay")
    ResponseEntity<OrderDto> payOrder(@PathVariable Long id);
}
