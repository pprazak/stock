package cz.rohlik.service.stock.mapper;

import cz.rohlik.service.stock.domain.OrderItem;
import cz.rohlik.service.stock.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "order", ignore = true)
    OrderItem map(OrderItemDto orderItemDto);

    OrderItemDto map(OrderItem orderItem);
}
