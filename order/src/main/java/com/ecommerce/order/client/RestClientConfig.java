package com.ecommerce.order.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Common RestClient configuration for all service clients.
 * 
 * WHY NEEDED:
 * - Multiple service clients (UserServiceClient, ProductServiceClient) need RestClient.Builder
 * - Spring doesn't allow duplicate bean definitions
 * - This centralizes the RestClient.Builder bean creation
 * 
 * PURPOSE:
 * - Creates a single, load-balanced RestClient.Builder bean
 * - @LoadBalanced enables Eureka service discovery (resolves service names to actual URLs)
 * - All service clients will inject this same builder instance
 */
@Configuration
public class RestClientConfig {
    
    /**
     * Creates a load-balanced RestClient.Builder bean.
     * 
     * @LoadBalanced annotation enables:
     * - Service discovery via Eureka
     * - Automatic load balancing across multiple service instances
     * - Resolving service names (e.g., "user-service") to actual URLs
     */
    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}