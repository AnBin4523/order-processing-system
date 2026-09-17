package com.example.orderservice.web;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemRequest;
import com.example.orderservice.entity.User;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderViewController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserRepository userRepository;

    public OrderViewController(OrderService orderService, ProductService productService,
                                UserRepository userRepository) {
        this.orderService = orderService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @GetMapping("/app/orders")
    public String listOrders(@RequestParam(defaultValue = "0") int page, Model model, Authentication authentication) {
        Long userId = currentUserId(authentication);
        Pageable pageable = PageRequest.of(page, 10);
        model.addAttribute("orders", orderService.listOrdersForUser(userId, pageable));
        return "orders";
    }

    @GetMapping("/app/orders/new")
    public String newOrderForm(Model model) {
        model.addAttribute("products", productService.listProducts(PageRequest.of(0, 100)).getContent());
        return "new-order";
    }

    @PostMapping("/app/orders/new")
    public String createOrder(@RequestParam Long productId, @RequestParam Integer quantity,
                               Authentication authentication) {
        User user = currentUser(authentication);
        CreateOrderRequest request = new CreateOrderRequest(
                user.getUsername(),
                user.getEmail(),
                "VND",
                List.of(new OrderItemRequest(productId, quantity))
        );
        orderService.createOrder(request, user.getId());
        return "redirect:/app/orders";
    }

    @PostMapping("/app/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, @RequestParam(defaultValue = "0") int page,
                               Authentication authentication) {
        orderService.cancelOrder(id, currentUserId(authentication));
        return "redirect:/app/orders?page=" + page;
    }

    private Long currentUserId(Authentication authentication) {
        return currentUser(authentication).getId();
    }

    private User currentUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + authentication.getName()));
    }
}