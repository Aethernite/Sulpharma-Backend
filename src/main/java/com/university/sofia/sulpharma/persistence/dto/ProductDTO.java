package com.university.sofia.sulpharma.persistence.dto;

import com.university.sofia.sulpharma.persistence.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The Product DTO
 * <p>
 * Contains the needed information for a single Product
 */
@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String imageUrl;
    private String description;
    private BigDecimal price;
    private boolean display;
    private double rating;
    private Product.Category category;
    private Product.Subcategory subcategory;
    private boolean promoted;
    private BigDecimal promotionPrice;

    public ProductDTO(
            String imageUrl,
            String description,
            BigDecimal price,
            boolean display,
            double rating,
            Product.Category category,
            Product.Subcategory subcategory,
            boolean promoted,
            BigDecimal promotionPrice
    ) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.display = display;
        this.rating = rating;
        this.category = category;
        this.subcategory = subcategory;
        this.promoted = promoted;
        this.promotionPrice = promotionPrice;
    }
}
