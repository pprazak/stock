package cz.rohlik.service.stock.mapper;

import cz.rohlik.service.stock.domain.Order;
import cz.rohlik.service.stock.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "orderItems", target = "orderItems")
    Order map(OrderDto orderDto);

    @Mapping(source = "orderItems", target = "orderItems")
    OrderDto map(Order order);
}
