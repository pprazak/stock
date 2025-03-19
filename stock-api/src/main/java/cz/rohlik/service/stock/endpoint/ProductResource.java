package cz.rohlik.service.stock.endpoint;

import cz.rohlik.service.stock.dto.ProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Products", description = "Operations related to products")
@RequestMapping("/products")
public interface ProductResource {

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product and return the created product's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product details")
    })
    ResponseEntity<ProductDto> createProduct(
            @Parameter(description = "Product details to be created.") @RequestBody @Valid ProductDto productDTO);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to be deleted.") @PathVariable Long id);

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update a product's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product details"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "ID of the product to be updated.") @PathVariable Long id,
            @Parameter(description = "Updated product details.") @RequestBody @Valid ProductDto productDTO);

    @GetMapping
    @Operation(summary = "List all products", description = "Retrieve a list of all products")
    @ApiResponse(responseCode = "200", description = "List of products retrieved successfully")
    ResponseEntity<List<ProductDto>> listProducts();
}
