package cz.rohlik.service.stock.service.impl;

import cz.rohlik.service.stock.domain.OrderStatus;
import cz.rohlik.service.stock.domain.Product;
import cz.rohlik.service.stock.repository.OrderRepository;
import cz.rohlik.service.stock.repository.ProductRepository;
import cz.rohlik.service.stock.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductServiceImpl(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        boolean isProductInActiveOrders = orderRepository.existsByOrderItemsProductIdAndStatusNotIn(productId, OrderStatus.getInactiveStatuses());
        if (isProductInActiveOrders) {
            throw new IllegalStateException("Cannot delete product that is part of an active order.");
        }
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantityInStock(updatedProduct.getQuantityInStock());

        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}

