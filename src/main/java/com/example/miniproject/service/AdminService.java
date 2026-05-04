package com.example.miniproject.service;

import com.example.miniproject.model.entity.Category;
import com.example.miniproject.model.entity.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final List<Category> categories = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();

    public AdminService() {
        Category phone = Category.builder().id(1L).name("Dien thoai").products(new ArrayList<>()).build();
        Category laptop = Category.builder().id(2L).name("Laptop").products(new ArrayList<>()).build();
        Category accessory = Category.builder().id(3L).name("Phu kien").products(new ArrayList<>()).build();

        categories.add(phone);
        categories.add(laptop);
        categories.add(accessory);

        addSeedProduct(1L, "iPhone 15", "Dien thoai Apple", 12, 19990000D, "/images/iphone.jpg", phone);
        addSeedProduct(2L, "Samsung Galaxy S24", "Dien thoai Samsung", 8, 17990000D, "/images/samsung.jpg", phone);
        addSeedProduct(3L, "MacBook Air M2", "Laptop Apple", 5, 24990000D, "/images/macbook.jpg", laptop);
        addSeedProduct(4L, "Chuot Logitech", "Phu kien may tinh", 30, 390000D, "/images/mouse.jpg", accessory);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Product> getProducts() {
        return products;
    }

    private void addSeedProduct(Long id, String name, String description, Integer stock, Double price, String image, Category category) {
        Product product = Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .stock(stock)
                .price(price)
                .image(image)
                .category(category)
                .build();
        products.add(product);
        category.getProducts().add(product);
    }
}
