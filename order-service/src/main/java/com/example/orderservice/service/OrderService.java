package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.entity.Product;
import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.event.OrderEventPublisher;
import com.example.orderservice.event.OrderStatusChangedEvent;
import com.example.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository, ProductService productService,
                         OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderEventPublisher = orderEventPublisher;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        return createOrder(request, null);
    }

    public OrderResponse createOrder(CreateOrderRequest request, Long userId) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setCustomerEmail(request.customerEmail());
        order.setCurrency(request.currency());
        order.setStatus(OrderStatus.PENDING);
        order.setUserId(userId);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productService.findProductOrThrow(itemRequest.productId());

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(product.getPrice());
            order.addItem(item);

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        orderEventPublisher.publishOrderCreated(new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getCustomerEmail(),
                savedOrder.getTotalAmount(),
                savedOrder.getCurrency()
        ));

        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        return OrderResponse.from(findOrderOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> listOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> listOrdersForUser(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(OrderResponse::from);
    }

    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = findOrderOrThrow(id);
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);

        if (oldStatus != status) {
            orderEventPublisher.publishOrderStatusChanged(
                    new OrderStatusChangedEvent(savedOrder.getId(), oldStatus, status));
        }

        return OrderResponse.from(savedOrder);
    }

    public OrderResponse cancelOrder(Long id) {
        return cancelOrder(findOrderOrThrow(id));
    }

    public OrderResponse cancelOrder(Long id, Long userId) {
        Order order = findOrderOrThrow(id);
        if (!Objects.equals(order.getUserId(), userId)) {
            // Treat another user's order as not found, rather than confirming it exists.
            throw new EntityNotFoundException("Order not found: " + id);
        }
        return cancelOrder(order);
    }

    private OrderResponse cancelOrder(Order order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING orders can be cancelled, current status: " + order.getStatus());
        }
        return updateStatus(order.getId(), OrderStatus.CANCELLED);
    }

    public void deleteOrder(Long id) {
        Order order = findOrderOrThrow(id);
        orderRepository.delete(order);
    }

    private Order findOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
    }
}