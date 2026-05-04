package com.example.miniproject.controller;

import com.example.miniproject.model.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/cart")
    public String checkout(
            HttpSession session,
            Model model
    ) {
        Map<Product, Integer> cart = (Map<Product, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);
        return "cart";
    }
}
