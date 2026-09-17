package com.example.orderservice.event;

import com.example.orderservice.entity.OrderStatus;

public record OrderStatusChangedEvent(
        Long orderId,
        OrderStatus oldStatus,
        OrderStatus newStatus
) {
}