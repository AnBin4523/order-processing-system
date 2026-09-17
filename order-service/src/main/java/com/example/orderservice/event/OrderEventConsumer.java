package com.example.orderservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = "order.created", groupId = "order-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Order created: id={}, customer={} <{}>, total={} {} -> sending confirmation email",
                event.orderId(), event.customerName(), event.customerEmail(),
                event.totalAmount(), event.currency());
    }

    @KafkaListener(topics = "order.status-changed", groupId = "order-service")
    public void onOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("Order {} status changed: {} -> {}",
                event.orderId(), event.oldStatus(), event.newStatus());
    }
}
