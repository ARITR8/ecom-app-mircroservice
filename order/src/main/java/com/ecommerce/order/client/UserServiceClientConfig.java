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
        
        RestClient restClient = restClientBuilder
                .baseUrl("http://user-service")
                .build();
        
        return id -> {
            try {
                return restClient.get()
                    .uri("/api/users/{id}", id)
                    .retrieve()
                    .body(UserResponse.class);
            } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
                // 404 - User not found (this is a valid business case, return null)
                return null;
            } catch (Exception e) {
                // Service unavailable, connection errors, etc. - throw exception for circuit breaker
                throw new RuntimeException("User service unavailable: " + e.getMessage(), e);
            }
        };
    }
}