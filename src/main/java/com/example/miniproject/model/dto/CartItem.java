package com.example.miniproject.model.dto;

import com.example.miniproject.model.entity.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItem {
    private Long id;
    private Product product;
    private Integer quantity;
}
