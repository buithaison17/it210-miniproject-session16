package com.example.miniproject.service;

import com.example.miniproject.model.entity.Category;
import com.example.miniproject.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    public Product findById(Long id) {
        return getSampleProducts().stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Page<Product> searchProducts(String keyword, Double minPrice, Double maxPrice, Long categoryId, int page, int size) {
        List<Product> filteredProducts = getSampleProducts().stream()
                .filter(product -> !StringUtils.hasText(keyword)
                        || product.getName().toLowerCase().contains(keyword.trim().toLowerCase()))
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .filter(product -> categoryId == null
                        || (product.getCategory() != null && categoryId.equals(product.getCategory().getId())))
                .toList();

        int start = Math.min(page * size, filteredProducts.size());
        int end = Math.min(start + size, filteredProducts.size());
        List<Product> pageContent = filteredProducts.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), filteredProducts.size());
    }

    public List<Category> findAllCategories() {
        return getSampleCategories();
    }

    private List<Category> getSampleCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().id(1L).name("Điện thoại").build());
        categories.add(Category.builder().id(2L).name("Laptop").build());
        categories.add(Category.builder().id(3L).name("Phụ kiện").build());
        return categories;
    }

    private List<Product> getSampleProducts() {
        List<Category> categories = getSampleCategories();
        Category phone = categories.get(0);
        Category laptop = categories.get(1);
        Category accessory = categories.get(2);

        List<Product> products = new ArrayList<>();
        products.add(Product.builder().id(1L).name("iPhone 15").description("Dien thoai Apple").stock(10).price(19990000.0).image("https://via.placeholder.com/400x300?text=iPhone+15").category(phone).build());
        products.add(Product.builder().id(2L).name("Samsung S24").description("Dien thoai Samsung").stock(8).price(18490000.0).image("https://via.placeholder.com/400x300?text=Samsung+S24").category(phone).build());
        products.add(Product.builder().id(3L).name("Xiaomi 14").description("Dien thoai Xiaomi").stock(15).price(13990000.0).image("https://via.placeholder.com/400x300?text=Xiaomi+14").category(phone).build());
        products.add(Product.builder().id(4L).name("MacBook Air M3").description("Laptop Apple mong nhe").stock(5).price(28990000.0).image("https://via.placeholder.com/400x300?text=MacBook+Air+M3").category(laptop).build());
        products.add(Product.builder().id(5L).name("Dell XPS 13").description("Laptop cao cap").stock(4).price(31990000.0).image("https://via.placeholder.com/400x300?text=Dell+XPS+13").category(laptop).build());
        products.add(Product.builder().id(6L).name("Asus Vivobook").description("Laptop cho sinh vien").stock(7).price(15990000.0).image("https://via.placeholder.com/400x300?text=Asus+Vivobook").category(laptop).build());
        products.add(Product.builder().id(7L).name("Chuot Logitech").description("Chuot khong day").stock(20).price(590000.0).image("https://via.placeholder.com/400x300?text=Chuot+Logitech").category(accessory).build());
        products.add(Product.builder().id(8L).name("Ban phim Co").description("Ban phim co RGB").stock(12).price(1290000.0).image("https://via.placeholder.com/400x300?text=Ban+phim+Co").category(accessory).build());
        products.add(Product.builder().id(9L).name("Tai nghe Sony").description("Tai nghe chong on").stock(9).price(3490000.0).image("https://via.placeholder.com/400x300?text=Tai+nghe+Sony").category(accessory).build());
        return products;
    }
}
