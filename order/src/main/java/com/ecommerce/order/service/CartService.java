package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.repository.CartItemRepository;
import com.ecommerce.order.client.UserServiceClient;
import com.ecommerce.order.dto.UserResponse;

@Service
@RequiredArgsConstructor
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final CartItemRepository cartItemRepository;
    private final UserServiceClient userServiceClient;
    
    public boolean addToCart(String userId, CartItemRequest request) {
        logger.debug("Adding to cart: userId={}, productId={}, quantity={}", 
            userId, request.getProductId(), request.getQuantity());
        
        // Validate user exists via User Service
        UserResponse userResponse = userServiceClient.getUserDetails(userId);
        
        if (userResponse == null) {
            logger.warn("User not found: userId={}", userId);
            return false;
        }
        
        // Check if cart item already exists
        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByUserIdAndProductId(
            userId, 
            request.getProductId()
        );
        
        if (existingCartItemOpt.isPresent()) {
            CartItem existingCartItem = existingCartItemOpt.get();
            int oldQuantity = existingCartItem.getQuantity();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(100.00)
                .multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
            logger.info("Cart item quantity updated: userId={}, productId={}, oldQuantity={}, newQuantity={}", 
                userId, request.getProductId(), oldQuantity, existingCartItem.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(100.00)
                .multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
            logger.info("New cart item added: userId={}, productId={}, quantity={}", 
                userId, request.getProductId(), request.getQuantity());
        }
        
        return true;
    }
    
    public List<CartItem> getCart(String userId) {
        logger.debug("Retrieving cart: userId={}", userId);
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        logger.debug("Cart items retrieved: userId={}, count={}", userId, cartItems.size());
        return cartItems;
    }
    
    @Transactional
    public boolean deleteItemFromCart(String userId, Long productId) {
        logger.debug("Deleting cart item: userId={}, productId={}", userId, productId);
        Optional<CartItem> cartItemOpt = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItemOpt.isPresent()) {
            cartItemRepository.delete(cartItemOpt.get());
            logger.info("Cart item deleted: userId={}, productId={}", userId, productId);
            return true;
        }
        logger.warn("Cart item not found for deletion: userId={}, productId={}", userId, productId);
        return false;
    }
    
    public void clearCart(String userId) {
    logger.debug("Clearing cart: userId={}", userId);
    cartItemRepository.deleteByUserId(userId);
    logger.info("Cart cleared: userId={}", userId);
}
}