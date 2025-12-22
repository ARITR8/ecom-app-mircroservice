package com.ecommerce.order.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.ecommerce.order.dto.UserResponse;

/**
 * Configuration class for UserServiceClient.
 * Uses direct RestClient approach (no HTTP Interface adapter needed).
 */
@Configuration
public class UserServiceClientConfig {
    
    @Bean
    public UserServiceClient userServiceClientInterface(RestClient.Builder restClientBuilder) {
        
        // Create RestClient instance
        RestClient restClient = restClientBuilder
                .baseUrl("http://user-service")  // Service name from Eureka
                .build();
        
        // Return a lambda implementation that uses RestClient directly
        // This avoids the adapter dependency issues
        return id -> {
            try {
                return restClient.get()
                    .uri("/api/users/{id}", id)
                    .retrieve()
                    .body(UserResponse.class);
            } catch (Exception e) {
                // Return null on any error (404, 500, etc.)
                return null;
            }
        };
    }
}