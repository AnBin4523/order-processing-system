package com.example.orderservice.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        String customerName,
        String customerEmail,
        BigDecimal totalAmount,
        String currency
) {
}