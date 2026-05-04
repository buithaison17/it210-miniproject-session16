package com.example.miniproject.controller;

import com.example.miniproject.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public String viewAdmin(Model model) {
        model.addAttribute("categories", adminService.getCategories());
        model.addAttribute("products", adminService.getProducts());
        return "admin";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", adminService.getCategories());
        return "admin/categories";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", adminService.getProducts());
        return "admin/products";
    }
}
