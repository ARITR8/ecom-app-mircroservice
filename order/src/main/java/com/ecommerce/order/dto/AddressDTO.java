package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing user address.
 * 
 * WHY NEEDED:
 * - UserResponse contains an AddressDTO field
 * - Must match the AddressDTO structure in User Service
 * 
 * PURPOSE:
 * - Represents user's address information
 * - Nested within UserResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}