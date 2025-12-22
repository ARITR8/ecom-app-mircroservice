package com.ecommerce.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ecommerce.product.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JPA Convention Method: findByActiveTrue()
    // JPA automatically generates: SELECT * FROM products WHERE active = true
    List<Product> findByActiveTrue();
    
    // Custom JPQL Query for searching products
    @Query("SELECT p FROM Product p WHERE p.active = true " +
           "AND p.quantity > 0 " +
           "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    // All CRUD methods automatically available:
    // - save(Product product)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
    // - count()
    // - existsById(Long id)
    // - And many more!
}