package com.ecommerce.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-Id") String userId) {
        
        logger.info("Order creation request received for userId: {}", userId);
        
        return orderService.createOrder(userId)
                .map(orderResponse -> {
                    logger.info("Order created successfully: orderId={}, userId={}, totalAmount={}", 
                        orderResponse.getId(), userId, orderResponse.getTotalAmount());
                    return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
                })
                .orElseGet(() -> {
                    logger.warn("Order creation failed: userId={}, reason=empty cart", userId);
                    return ResponseEntity.badRequest().build();
                });
    }
}