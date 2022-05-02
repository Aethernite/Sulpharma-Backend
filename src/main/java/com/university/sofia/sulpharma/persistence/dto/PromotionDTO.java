package com.university.sofia.sulpharma.persistence.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The Promotion DTO
 * <p>
 * Contains the needed information for a single Promotion
 */
@Data
@NoArgsConstructor
public class PromotionDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private LocalDate effectiveDate;
    private LocalDate termDate;

    public PromotionDTO(String name, String imageUrl, LocalDate effectiveDate, LocalDate termDate) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.effectiveDate = effectiveDate;
        this.termDate = termDate;
    }
}
