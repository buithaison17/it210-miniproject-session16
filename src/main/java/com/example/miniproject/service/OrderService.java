package com.example.miniproject.service;

import com.example.miniproject.model.entity.Order;
import com.example.miniproject.model.entity.OrderItem;
import com.example.miniproject.model.entity.Product;
import com.example.miniproject.repository.OrderItemRepository;
import com.example.miniproject.repository.OrderRepository;
import com.example.miniproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(Order order, Map<Long, Integer> cart) throws Exception {
        // 1. Lưu thông tin Order chung
        Order savedOrder = orderRepository.save(order);

        // 2. Duyệt giỏ hàng để xử lý từng sản phẩm
        for (Map.Entry<Long, Integer> item : cart.entrySet()) {
            Long productId = item.getKey();
            Integer quantityInCart = item.getValue();

            // Tìm sản phẩm trong DB
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new Exception("Sản phẩm mã " + productId + " không tồn tại!"));

            // 3. Kiểm tra tồn kho (Dùng biến stock theo Entity của bạn)
            if (product.getStock() < quantityInCart) {
                // Ném ngoại lệ để kích hoạt @Transactional Rollback
                throw new Exception("Sản phẩm " + product.getName() + " không đủ số lượng (Hiện còn: " + product.getStock() + ")");
            }

            // 4. Trừ tồn kho và cập nhật lại Product
            product.setStock(product.getStock() - quantityInCart);
            productRepository.save(product);

            // 5. Tạo và lưu chi tiết đơn hàng (OrderItem)
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantityInCart);
            orderItem.setPrice(product.getPrice()); 

            orderItemRepository.save(orderItem);
        }
    }
}