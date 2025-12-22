package com.ecommerce.user.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;

import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")  // Base URL
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    // Get all users - returns 200 OK
    @GetMapping  // Resolves to /api/users
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }
    
    // Get single user - returns 200 OK or 404 NOT_FOUND
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        return userService.fetchUser(id)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
    
    // Create user - returns 200 OK (or 201 CREATED)
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody  UserRequest request) {
        userService.addUser(request);
        return ResponseEntity.ok("User added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable String id, 
            @RequestBody UserRequest request) {
        
        boolean updated = userService.updateUser(id, request);
        
        if (updated) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}