package cz.rohlik.service.stock.controller;

import cz.rohlik.service.stock.dto.ProductDto;
import cz.rohlik.service.stock.endpoint.ProductResource;
import cz.rohlik.service.stock.mapper.ProductMapper;
import cz.rohlik.service.stock.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController implements ProductResource {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public ResponseEntity<ProductDto> createProduct(ProductDto productDTO) {
        var product = productMapper.map(productDTO);
        var createdProduct = productService.createProduct(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.map(createdProduct));
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDTO) {
        var product = productMapper.map(productDTO);
        var updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(productMapper.map(updatedProduct));
    }

    @Override
    public ResponseEntity<List<ProductDto>> listProducts() {
        var products = productService.getProducts();
        var productDtos = products.stream().map(productMapper::map).toList();
        return ResponseEntity.ok(productDtos);
    }
}
