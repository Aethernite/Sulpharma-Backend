package com.university.sofia.sulpharma.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Promotion extends BaseEntity {

    @NotEmpty
    private String name;

    @NotEmpty
    private String imageUrl;

    @NotNull
    private LocalDate effectiveDate;

    @NotNull
    private LocalDate termDate;

    public void update(Promotion promotion) {
        this.imageUrl = promotion.getImageUrl();
        this.effectiveDate = promotion.getEffectiveDate();
        this.termDate = promotion.getTermDate();
    }
}
