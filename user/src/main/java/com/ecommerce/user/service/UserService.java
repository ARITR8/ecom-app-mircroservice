package com.ecommerce.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.ecommerce.user.model.User;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    // Return DTOs instead of entities
    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll()
                            .stream()
                            .map(this::mapToUserResponse)  // Convert Entity → DTO
                            .collect(Collectors.toList());
    }
    
    // Accept DTO, convert to Entity, save
    public void addUser(UserRequest request) {
        User user = new User();
        updateUserFromRequest(user, request);
        userRepository.save(user);
    }
    
    // Return DTO instead of Entity
    public Optional<UserResponse> fetchUser(String id) {
        return userRepository.findById(id)
                            .map(this::mapToUserResponse);  // Convert Entity → DTO
    }
    
    // Accept DTO, update Entity
    public boolean updateUser(String id, UserRequest request) {
        return userRepository.findById(id)
                            .map(existingUser -> {
                                updateUserFromRequest(existingUser, request);
                                userRepository.save(existingUser);
                                return true;
                            })
                            .orElse(false);
    }
    
    // Mapping: Entity → DTO
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        
        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipCode(user.getAddress().getZipCode());
            response.setAddress(addressDTO);
        }
        
        return response;
    }
    
    // Mapping: DTO → Entity
    private void updateUserFromRequest(User user, UserRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        
        if (request.getAddress() != null) {
            Address address = new Address();
            address.setStreet(request.getAddress().getStreet());
            address.setCity(request.getAddress().getCity());
            address.setState(request.getAddress().getState());
            address.setCountry(request.getAddress().getCountry());
            address.setZipCode(request.getAddress().getZipCode());
            user.setAddress(address);  // Important!
        }
    }
}