package cz.rohlik.service.stock.mapper;

import cz.rohlik.service.stock.domain.Product;
import cz.rohlik.service.stock.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product map(ProductDto productDto);

    ProductDto map(Product product);
}
