package cz.rohlik.service.stock.repository;

import cz.rohlik.service.stock.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("UPDATE Product p SET p.quantityInStock = p.quantityInStock - :quantity WHERE p.id = :productId AND p.quantityInStock >= :quantity")
    int decreaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE Product p SET p.quantityInStock = p.quantityInStock + :quantity WHERE p.id = :productId")
    void releaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

}
