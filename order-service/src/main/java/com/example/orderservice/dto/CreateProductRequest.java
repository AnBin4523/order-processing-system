package com.example.orderservice.dto;

import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        BigDecimal price,
        String currency
) {
}