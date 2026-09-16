package com.example.orderservice.dto;

public record OrderItemRequest(
        Long productId,
        Integer quantity
) {
}