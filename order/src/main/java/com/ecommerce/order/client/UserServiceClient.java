package com.ecommerce.order.client;

import com.ecommerce.order.dto.UserResponse;

/**
 * Client interface for communicating with User Service.
 * 
 * Uses functional interface pattern with direct RestClient implementation
 * instead of HTTP Interface to avoid adapter dependency issues.
 */
@FunctionalInterface
public interface UserServiceClient {
    
    /**
     * Gets user details by user ID.
     * 
     * @param id User ID (as String)
     * @return UserResponse if user exists, null if user doesn't exist
     */
    UserResponse getUserDetails(String id);
}