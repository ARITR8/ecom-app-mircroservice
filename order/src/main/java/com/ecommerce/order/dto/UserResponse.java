package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing User information from User Service.
 * 
 * WHY NEEDED:
 * - Order Service receives UserResponse from User Service API
 * - Need this class to deserialize JSON response into Java object
 * - Must match the structure of UserResponse in User Service
 * 
 * PURPOSE:
 * - Represents user data structure
 * - Used to validate if user exists
 * - Contains user details (name, email, role, address)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;              // User ID
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;           // Enum: CUSTOMER or ADMIN
    private AddressDTO address;     // Nested DTO for address
}