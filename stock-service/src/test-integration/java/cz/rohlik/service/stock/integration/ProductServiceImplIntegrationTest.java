package cz.rohlik.service.stock.integration;

import cz.rohlik.service.stock.domain.Product;
import cz.rohlik.service.stock.repository.ProductRepository;
import cz.rohlik.service.stock.service.impl.ProductServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProductServiceImplIntegrationTest {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        var product = new Product()
                .setName("Test Product")
                .setQuantityInStock(10)
                .setPrice(new BigDecimal("100.0"));
        productRepository.save(product);
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() {
        var product = new Product()
                .setName("New Product")
                .setQuantityInStock(20)
                .setPrice(new BigDecimal("200.0"));

        var createdProduct = productService.createProduct(product);

        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo("New Product");
    }

    @Test
    void deleteProduct_shouldRemoveProduct() {
        var product = productRepository.findAll().getFirst();

        productService.deleteProduct(product.getId());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() {
        var product = productRepository.findAll().getFirst();
        var updatedProduct = new Product()
                .setName("Updated Product")
                .setQuantityInStock(20)
                .setPrice(new BigDecimal("150.0"));

        var result = productService.updateProduct(product.getId(), updatedProduct);

        assertThat(result.getName()).isEqualTo("Updated Product");
        assertThat(result.getQuantityInStock()).isEqualTo(20);
    }

    @Test
    void updateProduct_shouldThrowException_whenProductNotFound() {
        var updatedProduct = new Product()
                .setName("Updated Product")
                .setQuantityInStock(20)
                .setPrice(new BigDecimal("150.0"));

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(999L, updatedProduct));
    }

    @Test
    void getProducts_shouldReturnProductList() {
        var products = productService.getProducts();

        assertThat(products).isNotEmpty();
        assertThat(products.getFirst().getName()).isEqualTo("Test Product");
    }
}

