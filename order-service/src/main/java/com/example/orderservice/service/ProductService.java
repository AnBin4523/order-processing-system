package com.example.orderservice.service;

import com.example.orderservice.dto.CreateProductRequest;
import com.example.orderservice.dto.ProductResponse;
import com.example.orderservice.entity.Product;
import com.example.orderservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setCurrency(request.currency());
        product.setImageUrl(request.imageUrl() != null && !request.imageUrl().isBlank()
                ? request.imageUrl()
                : defaultImageUrl(request.name()));
        return ProductResponse.from(productRepository.save(product));
    }

    private String defaultImageUrl(String name) {
        String encoded = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
        return "https://placehold.co/400x300?text=" + encoded;
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        return ProductResponse.from(findProductOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> listProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponse::from);
    }

    Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }
}