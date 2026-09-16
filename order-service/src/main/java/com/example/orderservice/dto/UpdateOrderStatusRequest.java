package com.example.orderservice.dto;

import com.example.orderservice.entity.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus status
) {
}