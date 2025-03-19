package cz.rohlik.service.stock.mapper;

import cz.rohlik.service.stock.domain.Order;
import cz.rohlik.service.stock.dto.OrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    Order map(OrderDto orderDto);

    OrderDto map(Order order);
}
