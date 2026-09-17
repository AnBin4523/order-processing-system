package com.example.orderservice.web;

import com.example.orderservice.entity.OrderStatus;
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
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/orders")
    public String listOrders(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 15);
        model.addAttribute("orders", orderService.listOrders(pageable));
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/admin/orders/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam OrderStatus status,
                                @RequestParam(defaultValue = "0") int page) {
        orderService.updateStatus(id, status);
        return "redirect:/admin/orders?page=" + page;
    }
}
