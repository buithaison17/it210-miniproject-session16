package com.example.miniproject.controller;

import com.example.miniproject.model.entity.Order;
import com.example.miniproject.model.entity.Product;
import com.example.miniproject.repository.ProductRepository;
import com.example.miniproject.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/cart")
    public String showCartPage(HttpSession session, Model model) {
        // Lấy giỏ hàng từ Session (Lưu dạng Map ID -> Số lượng)
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        // Chuyển đổi dữ liệu để hiển thị (Cần Object Product để lấy Tên, Ảnh, Giá)
        Map<Product, Integer> cartWithObject = new HashMap<>();
        double totalPrice = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product p = productRepository.findById(entry.getKey()).orElse(null);
            if (p != null) {
                cartWithObject.put(p, entry.getValue());
                totalPrice += p.getPrice() * entry.getValue();
            }
        }

        model.addAttribute("cart", cartWithObject);
        model.addAttribute("totalPrice", totalPrice);

        return "cart"; // Trả về cart.html
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        // 1. Lấy giỏ hàng (bây giờ Leader để là List/Collection nên ta dùng Object cho an toàn hoặc List)
        Object cartObj = session.getAttribute("cart");

        if (cartObj == null) {
            return "redirect:/cart";
        }

        // 2. Tính tổng tiền dựa trên cấu trúc mới (thường Leader sẽ dùng List các đối tượng CartItem)
        double total = 0;

        // Nếu Leader dùng List<CartItem> hoặc tương đương:
        if (cartObj instanceof List<?>) {
            List<?> cartList = (List<?>) cartObj;
            if (cartList.isEmpty()) return "redirect:/cart";

            // Bạn cần xem CartItem của Leader có các method nào (ví dụ getPrice, getQuantity)
            // Đây là code giả định phổ biến sau khi merge:
            model.addAttribute("cart", cartList);
            // Tính tổng tiền (Ví dụ: duyệt list và cộng dồn)
            // total = cartList.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        }

        model.addAttribute("totalPrice", total);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(Order order, HttpSession session, RedirectAttributes ra) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("errorMsg", "Giỏ hàng của bạn đang trống!");
            return "redirect:/cart";
        }

        try {
            // Gửi dữ liệu xuống Service để xử lý @Transactional
            // Bước này bao gồm: Lưu Order -> Lưu OrderItem -> Trừ Stock sản phẩm
            orderService.placeOrder(order, cart);

            // Nếu thành công: Xóa giỏ hàng trong Session và thông báo
            session.removeAttribute("cart");
            ra.addFlashAttribute("successMsg", "Đặt hàng thành công! Cảm ơn bạn đã mua sắm.");
            return "redirect:/home"; // Quay về trang chủ hoặc trang danh sách sản phẩm

        } catch (Exception e) {
            // Nếu thất bại (Ví dụ: Hết hàng - Out of stock):
            // Transaction sẽ tự động Rollback nhờ @Transactional trong Service
            // Gửi thông báo lỗi cụ thể về trang giỏ hàng để khách chỉnh sửa
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
            return "redirect:/cart";
        }
    }

}