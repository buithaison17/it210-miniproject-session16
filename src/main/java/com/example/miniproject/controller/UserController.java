package com.example.miniproject.controller;

import com.example.miniproject.model.dto.CartItem;
import com.example.miniproject.model.entity.Product;
import com.example.miniproject.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final ProductService productService;

    public UserController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/cart")
    public String checkout(
            HttpSession session,
            Model model
    ) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        // Tính tổng tiền sản phẩm
        double total = cart.stream().mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice()).sum();
        // Phí ship
        double shippingFee = 30000;

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        model.addAttribute("shippingFee", shippingFee);
        return "cart";
    }

    // Xoá sản phẩm khỏi giỏ hàng
    @GetMapping("/delete-cart")
    public String deleteCart(
            @RequestParam("id") Long id,
            HttpSession session
    ) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        cart.removeIf(item -> item.getProduct().getId().equals(id));
        session.setAttribute("cart", cart);
        return "redirect:/user/cart";
    }

    // Tăng số lượng
    @GetMapping("/increase-item-cart")
    public String increaseItemCart(
            @RequestParam("id") Long id,
            HttpSession session
    ) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        CartItem cartItem = cart.stream().filter(item -> item.getProduct().getId().equals(id)).findFirst().orElse(null);
        Product product = productService.findById(id);
        if (cartItem != null && cartItem.getQuantity() + 1 <= product.getStock()) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            session.setAttribute("cart", cart);
        }
        return "redirect:/user/cart";
    }

    // Giảm số lượng
    @GetMapping("/decrease-item-cart")
    public String decreaseItemCart(
            @RequestParam("id") Long id,
            HttpSession session
    ) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        CartItem cartItem = cart.stream().filter(item -> item.getProduct().getId().equals(id)).findFirst().orElse(null);
        if (cartItem != null && cartItem.getQuantity() - 1 == 0) {
            deleteCart(id, session);
        } else if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            session.setAttribute("cart", cart);
        }
        return "redirect:/user/cart";
    }
}
