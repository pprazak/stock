package cz.rohlik.service.stock.mapper;

import cz.rohlik.service.stock.domain.OrderItem;
import cz.rohlik.service.stock.dto.OrderItemDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem map(OrderItemDto orderItemDto);

    OrderItemDto map(OrderItem orderItem);
}
