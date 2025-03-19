package cz.rohlik.service.stock.mapper;

import cz.rohlik.service.stock.domain.Product;
import cz.rohlik.service.stock.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "version", ignore = true)
    Product map(ProductDto productDto);

    ProductDto map(Product product);
}
