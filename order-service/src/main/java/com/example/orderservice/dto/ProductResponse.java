package com.example.orderservice.dto;

import com.example.orderservice.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String currency,
        String imageUrl
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                product.getCurrency(), product.getImageUrl());
    }
}