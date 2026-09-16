package com.example.orderservice.dto;

import java.util.List;

public record CreateOrderRequest(
        String customerName,
        String customerEmail,
        String currency,
        List<OrderItemRequest> items
) {
}