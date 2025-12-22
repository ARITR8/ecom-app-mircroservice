package com.ecommerce.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final CartService cartService;
    
    @Transactional
public Optional<OrderResponse> createOrder(String userId) {
    logger.debug("Creating order for userId: {}", userId);
    
    List<CartItem> cartItems = cartService.getCart(userId);
    if (cartItems.isEmpty()) {
        logger.warn("Order creation failed: userId={}, reason=cart is empty", userId);
        return Optional.empty();
    }

    logger.debug("Cart items found for userId={}: {}", userId, cartItems.size());
    
    // Calculate total price from cart items
    BigDecimal totalPrice = cartItems.stream()
            .map(CartItem::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    // Create order
    Order order = new Order();
    order.setUserId(userId);
    order.setTotalAmount(totalPrice);
    order.setOrderStatus(OrderStatus.CONFIRMED);
    
    // Convert cart items to order items
    List<OrderItem> orderItems = cartItems.stream()
            .map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(item.getPrice());
                orderItem.setOrder(order);
                return orderItem;
            })
            .collect(Collectors.toList());
    
    order.setItems(orderItems);
    
    try {
        // Save order (cascade will save order items)
        Order savedOrder = orderRepository.save(order);
        logger.info("Order saved successfully: orderId={}, userId={}, totalAmount={}", 
            savedOrder.getId(), userId, savedOrder.getTotalAmount());
        
        // Clear the cart after order is placed
        cartService.clearCart(userId);
        logger.debug("Cart cleared for userId: {}", userId);
        
        // Convert Order to OrderResponse
        return Optional.of(mapToOrderResponse(savedOrder));
    } catch (Exception e) {
        logger.error("Error creating order: userId={}, error={}", userId, e.getMessage(), e);
        throw e; // Re-throw to maintain transaction rollback
    }
}
    // Helper method: Convert Order entity to OrderResponse DTO
    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) {
            return new OrderResponse(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getOrderStatus(),
                    List.of(),
                    order.getCreatedAt()
            );
        }
        
        List<OrderItemDTO> orderItemDTOs = items.stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .collect(Collectors.toList());
        
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                orderItemDTOs,
                order.getCreatedAt()
        );
    }

    
}