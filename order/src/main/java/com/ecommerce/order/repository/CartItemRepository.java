package com.ecommerce.order.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ecommerce.order.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Changed userId parameter to String
    Optional<CartItem> findByUserIdAndProductId(String userId, Long productId);
    
    List<CartItem> findByUserId(String userId);  // Changed to String
    
    void deleteByUserIdAndProductId(String userId, Long productId);  // Changed to String
    
    //void deleteByUserId(String userId);  // Changed to String

    // In CartItemRepository
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId")
    int deleteByUserId(@Param("userId") String userId);
}
    
    // All CRUD methods automatically available:
    // - save(CartItem cartItem)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
    // - count()
    // - existsById(Long id)
    // - And many more!
