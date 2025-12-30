package com.ecommerce.notification.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.ecommerce.notification.config.RabbitMQConfig;
import com.ecommerce.notification.dto.OrderCreatedEvent;
import com.ecommerce.notification.service.NotificationService;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final NotificationService notificationService;
    
    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        logger.info("Received order created event: orderId={}, userId={}", 
            event.getOrderId(), event.getUserId());
        
        try {
            // Process notifications asynchronously
            notificationService.sendOrderConfirmationEmail(event);
            notificationService.sendOrderConfirmationSMS(event);
            notificationService.generateInvoice(event);
            
            logger.info("Successfully processed notifications for orderId={}", event.getOrderId());
        } catch (Exception e) {
            logger.error("Error processing notifications for orderId={}: {}", 
                event.getOrderId(), e.getMessage(), e);
            // In production, you might want to send to a dead letter queue
        }
    }
}