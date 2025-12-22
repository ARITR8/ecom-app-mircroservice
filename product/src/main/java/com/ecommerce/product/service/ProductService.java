package com.ecommerce.product.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.ecommerce.product.model.Product;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    
    // Create product
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        updateProductFromRequest(product, request);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }
    
    // Update product
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest request) {
        return productRepository.findById(id)
                               .map(existingProduct -> {
                                   updateProductFromRequest(existingProduct, request);
                                   Product savedProduct = productRepository.save(existingProduct);
                                   return mapToProductResponse(savedProduct);
                               });
    }
    
    // Get all products
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
    
    // Delete product (soft delete)
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }
    
    // Search products
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword)
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
    
    // Mapping: Entity → DTO
    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.isActive());
        return response;
    }
    
    // Mapping: DTO → Entity
    private void updateProductFromRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        // active defaults to true (set in entity)
        // id is auto-generated
        // timestamps are auto-managed
    }
}