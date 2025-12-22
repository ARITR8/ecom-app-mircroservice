package com.ecommerce.order.dto;

/**
 * Enum representing user roles.
 * 
 * WHY NEEDED:
 * - UserResponse contains a UserRole field
 * - Must match the UserRole enum in User Service
 * 
 * PURPOSE:
 * - Defines possible user roles (CUSTOMER, ADMIN)
 */
public enum UserRole {
    CUSTOMER,  // Regular customer
    ADMIN      // Administrator
}