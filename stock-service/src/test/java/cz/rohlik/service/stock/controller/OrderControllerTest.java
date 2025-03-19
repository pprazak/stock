package cz.rohlik.service.stock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rohlik.service.stock.domain.Order;
import cz.rohlik.service.stock.dto.CreateOrderRequest;
import cz.rohlik.service.stock.dto.OrderDto;
import cz.rohlik.service.stock.dto.OrderItemRequest;
import cz.rohlik.service.stock.mapper.OrderMapper;
import cz.rohlik.service.stock.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private Order order;
    private OrderDto orderDto;
    private CreateOrderRequest createOrderRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new OrderController(orderService, orderMapper))
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .build();

        orderDto = new OrderDto(1L, "PENDING", List.of(), LocalDateTime.now(), null);
        createOrderRequest = new CreateOrderRequest(List.of(new OrderItemRequest(1L, 2)));
    }

    @Test
    void createOrder_shouldReturnCreatedOrder() throws Exception {
        given(orderService.createOrder(createOrderRequest)).willReturn(order);
        given(orderMapper.map(order)).willReturn(orderDto);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderDto.id()))
                .andExpect(jsonPath("$.status").value(orderDto.status()))
                .andExpect(jsonPath("$.orderItems").isArray());
    }

    @Test
    void cancelOrder_shouldReturnCanceledOrder() throws Exception {
        given(orderService.cancelOrder(1L)).willReturn(order);
        given(orderMapper.map(order)).willReturn(orderDto);

        mockMvc.perform(patch("/orders/{id}/cancel", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderDto.id()))
                .andExpect(jsonPath("$.status").value(orderDto.status()));
    }

    @Test
    void payOrder_shouldReturnPaidOrder() throws Exception {
        given(orderService.setOrderPaid(1L)).willReturn(order);
        given(orderMapper.map(order)).willReturn(orderDto);

        mockMvc.perform(patch("/orders/{id}/pay", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderDto.id()))
                .andExpect(jsonPath("$.status").value(orderDto.status()));
    }

    @Test
    void createOrder_shouldReturnBadRequest_whenInvalidOrderDetails() throws Exception {
        var invalidRequest = new CreateOrderRequest(List.of());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Validation error"));
    }

    @Test
    void cancelOrder_shouldReturnNotFound_whenOrderNotExist() throws Exception {
        doThrow(new EntityNotFoundException("Order not found"))
                .when(orderService).cancelOrder(1L);

        mockMvc.perform(patch("/orders/{id}/cancel", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Entity not found"))
                .andExpect(jsonPath("$.description").value("Order not found"));
    }

}
