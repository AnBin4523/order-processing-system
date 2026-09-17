package com.example.orderservice.web;

import com.example.orderservice.dto.CreateProductRequest;
import com.example.orderservice.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/admin/products")
    public String listProducts(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 12);
        model.addAttribute("products", productService.listProducts(pageable));
        return "admin/products";
    }

    @PostMapping("/admin/products")
    public String createProduct(@RequestParam String name, @RequestParam BigDecimal price,
                                 @RequestParam String currency, @RequestParam(required = false) String imageUrl) {
        productService.createProduct(new CreateProductRequest(name, price, currency, imageUrl));
        return "redirect:/admin/products";
    }
}
