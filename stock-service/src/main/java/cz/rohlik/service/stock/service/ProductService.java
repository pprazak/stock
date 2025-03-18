package cz.rohlik.service.stock.service;

import cz.rohlik.service.stock.domain.Product;

import java.util.List;

public interface ProductService {

    /**
     * Creates a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    Product createProduct(Product product);

    /**
     * Deletes a product by its ID.
     *
     * @param productId the ID of the product to delete
     */
    void deleteProduct(Long productId);

    /**
     * Updates a product.
     *
     * @param productId      the ID of the product to update
     * @param updatedProduct the updated product
     * @return the updated product
     */
    Product updateProduct(Long productId, Product updatedProduct);

    /**
     * Retrieves all products.
     *
     * @return a list of all products
     */
    List<Product> getProducts();

}
