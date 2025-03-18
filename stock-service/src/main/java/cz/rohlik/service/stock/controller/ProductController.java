package cz.rohlik.service.stock.controller;

import cz.rohlik.service.stock.dto.ProductDto;
import cz.rohlik.service.stock.endpoint.ProductResource;
import cz.rohlik.service.stock.mapper.ProductMapper;
import cz.rohlik.service.stock.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product details")
    })
    public ResponseEntity<ProductDto> createProduct(
            @Parameter(description = "Product details to be created.") @Valid ProductDto productDTO) {
        var product = productMapper.map(productDTO);
        var createdProduct = productService.createProduct(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMapper.map(createdProduct));
    }

    @Override
    @Operation(summary = "Delete a product", description = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to be deleted.") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Operation(summary = "Update a product", description = "Update a product's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product details"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "ID of the product to be updated.") Long id,
            @Parameter(description = "Updated product details.") @Valid ProductDto productDTO) {
        var product = productMapper.map(productDTO);
        var updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(productMapper.map(updatedProduct));
    }

    @Override
    @Operation(summary = "List all products", description = "Retrieve a list of all products")
    @ApiResponse(responseCode = "200", description = "List of products retrieved successfully")
    public ResponseEntity<List<ProductDto>> listProducts() {
        var products = productService.getProducts();
        var productDtos = products.stream().map(productMapper::map).toList();
        return ResponseEntity.ok(productDtos);
    }
}
