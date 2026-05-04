package com.example.miniproject.controller;

import com.example.miniproject.enums.Role;
import com.example.miniproject.model.dto.UserDTO;
import com.example.miniproject.model.entity.User;
import com.example.miniproject.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return redirectBasedOnRole(user);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            var userOpt = authService.login(userDTO.getEmail(), userDTO.getPassword());
            if (userOpt.isPresent()) {
                session.setAttribute("user", userOpt.get());
                return "redirect:" + redirectBasedOnRole(userOpt.get());
            } else {
                model.addAttribute("userDTO", userDTO); // Giữ email
                model.addAttribute("error", "Email hoặc mật khẩu không hợp lệ");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("error", "Đăng nhập thất bại: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegister(HttpSession session) {
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return redirectBasedOnRole(user);
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDTO userDTO, Model model, RedirectAttributes redirectAttributes) {
        try {
            authService.register(userDTO);
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công. Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("userDTO", userDTO); // Giữ dữ liệu form
            model.addAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String redirectBasedOnRole(User user) {
        if (user.getRole() == Role.ADMIN) {
            return "/admin";
        } else {
            return "/"; // Assuming home page for customers
        }
    }
}
