package cz.rohlik.service.stock.service.impl;

import cz.rohlik.service.stock.domain.Product;
import cz.rohlik.service.stock.repository.OrderRepository;
import cz.rohlik.service.stock.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product().setName("Test Product").setPrice(new BigDecimal("99.99")).setQuantityInStock(10);
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        var createdProduct = productService.createProduct(product);

        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getId()).isEqualTo(product.getId());
        assertThat(createdProduct.getName()).isEqualTo(product.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(createdProduct.getQuantityInStock()).isEqualTo(product.getQuantityInStock());

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteProduct_shouldDeleteProduct_whenNotInActiveOrders() {
        when(orderRepository.existsByOrderItemsProductIdAndStatusNotIn(anyLong(), anySet())).thenReturn(false);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductIsInActiveOrders() {
        when(orderRepository.existsByOrderItemsProductIdAndStatusNotIn(anyLong(), anySet())).thenReturn(true);

        var exception = assertThrows(IllegalStateException.class, () -> productService.deleteProduct(1L));
        assertThat(exception.getMessage()).isEqualTo("Cannot delete product that is part of an active order.");

        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        var updatedProduct = new Product().setId(1L).setName("Updated Product").setPrice(new BigDecimal("150.00")).setQuantityInStock(20);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Product");
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(result.getQuantityInStock()).isEqualTo(20);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Product updatedProduct = new Product();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(1L, updatedProduct));
        assertThat(exception.getMessage()).isEqualTo("Product not found with id: 1");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getProducts_shouldReturnListOfProducts() {
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getProducts();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(products);

        verify(productRepository, times(1)).findAll();
    }
}
