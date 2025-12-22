package com.ecommerce.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;
    
    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CartItemRequest request) {
        
        logger.info("Add to cart request: userId={}, productId={}, quantity={}", 
            userId, request.getProductId(), request.getQuantity());
        
        boolean added = cartService.addToCart(userId, request);
        
        if (!added) {
            logger.warn("Failed to add to cart: userId={}, productId={}, reason=product out of stock or user/product not found", 
                userId, request.getProductId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Product out of stock or user not found or product not found");
        }
        
        logger.info("Item added to cart successfully: userId={}, productId={}", 
            userId, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-Id") String userId) {
        
        logger.debug("Get cart request: userId={}", userId);
        List<CartItem> cartItems = cartService.getCart(userId);
        logger.info("Cart retrieved: userId={}, itemCount={}", userId, cartItems.size());
        return ResponseEntity.ok(cartItems);
    }
    
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long productId) {
        
        logger.info("Remove from cart request: userId={}, productId={}", userId, productId);
        
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        
        if (deleted) {
            logger.info("Item removed from cart: userId={}, productId={}", userId, productId);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Item not found in cart: userId={}, productId={}", userId, productId);
            return ResponseEntity.notFound().build();
        }
    }
}