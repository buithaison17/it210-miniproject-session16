package com.example.miniproject.controller;

import com.example.miniproject.model.entity.Product;
import com.example.miniproject.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class ProductController {
    private static final int PAGE_SIZE = 6;

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/cilent")
    public String showProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<Product> productPage = productService.searchProducts(keyword, minPrice, maxPrice, categoryId, page, PAGE_SIZE);

        model.addAttribute("productPage", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", productService.findAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("currentPage", page);
        return "checkout";
    }
}
