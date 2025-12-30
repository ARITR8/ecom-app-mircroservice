package com.ecommerce.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.ecommerce.order.config.RabbitMQConfig;
import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;

@Service
@RequiredArgsConstructor
public class OrderEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;
    
    public void publishOrderCreatedEvent(Order order) {
        try {
            // Convert Order entity to OrderCreatedEvent DTO
            OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getOrderStatus().toString(),
                order.getCreatedAt(),
                order.getItems().stream()
                    .map(item -> new OrderCreatedEvent.OrderItemEvent(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice()
                    ))
                    .toList()
            );
            
            // Publish to RabbitMQ
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                event
            );
            
            logger.info("Order created event published: orderId={}, userId={}", 
                order.getId(), order.getUserId());
        } catch (Exception e) {
            logger.error("Failed to publish order created event: orderId={}, error={}", 
                order.getId(), e.getMessage(), e);
            // Don't throw exception - order is already saved, notification failure shouldn't fail order
        }
    }
}