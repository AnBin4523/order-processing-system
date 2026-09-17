package com.example.orderservice.web;

import com.example.orderservice.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderViewController {

    private final OrderService orderService;

    public OrderViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/app/orders")
    public String listOrders(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        model.addAttribute("orders", orderService.listOrders(pageable));
        return "orders";
    }

    @PostMapping("/app/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, @RequestParam(defaultValue = "0") int page) {
        orderService.cancelOrder(id);
        return "redirect:/app/orders?page=" + page;
    }
}
