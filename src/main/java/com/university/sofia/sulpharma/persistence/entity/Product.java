package com.university.sofia.sulpharma.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Product extends BaseEntity {

    @NotEmpty
    @Size(max = 255)
    private String imageUrl;

    @NotEmpty
    @Size(max = 120)
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private boolean promoted;

    @NotNull
    private BigDecimal promotionPrice;

    @NotNull
    private boolean display;

    @NotNull
    private Category category;

    @NotNull
    private Subcategory subcategory;

    @NotNull
    private Double rating;

    public void update(Product updated) {
        this.imageUrl = updated.getImageUrl();
        this.description = updated.getDescription();
        this.price = updated.getPrice();
        this.display = updated.isDisplay();
        this.category = updated.getCategory();
        this.subcategory = updated.getSubcategory();
        this.promoted = updated.isPromoted();
        this.promotionPrice = updated.getPromotionPrice();
    }

    @Getter
    public enum Category {
        HEALTH("HEALTH"),
        BEAUTY("BEAUTY"),
        MOTHER_CHILD("MOTHER-AND-CHILD");

        private static final HashMap<String, Category> MAP = new HashMap<>();
        private String label;

        static {
            for (Category category : Category.values()) {
                MAP.put(category.getLabel(), category);
            }
        }

        Category(String label) {
            this.label = label;
        }

        public static Category fromLabel(String label) {
            return MAP.get(label);
        }
    }

    @Getter
    public enum Subcategory {
        //HEALTH
        ALERGY("ALERGY"),
        VEINS_AND_HEMORRHOIDS("VEINS-AND-HEMORRHOIDS"),
        VITAMIN_AND_MINERALS("VITAMIN-AND-MINERALS"),
        FUNGUS("FUNGUS"),
        DIET("DIET"),
        DETOX("DETOX"),
        COUGH("COUGH"),
        PAIN("PAIN"),
        NERVE_SYSTEM("NERVE-SYSTEM"),
        FLU("FLU"),
        EYES("EYES"),
        BONES("BONES"),
        FEVER("FEVER"),
        EARS_NOSE_THROAT("EARS-NOSE-THROAT"),
        LIVER_KIDNEY("LIVER-AND-KIDNEY"),
        DIGESTING_SYSTEM("DIGESTING-SYSTEM"),
        DIABETES("DIABETES"),

        //BEAUTY
        DEODORANTS("DEODORANTS"),
        BABY("BABY"),
        COSMETICS("COSMETICS"),
        FACE("FACE"),
        MEN("MEN"),
        BODY("BODY"),
        LIPS("LIPS"),
        HAIR("HAIR"),
        SOAP("SOAP"),
        SUNBATHING("SUNBATHING"),
        OTHER_BEAUTY("OTHER-BEAUTY"),

        //MOTHER AND CHILD
        ASPIRATORS("ASPIRATORS"),
        PUMPS("PUMPS"),
        FOOD("FOOD"),
        TAMPONS("TAMPONS"),
        PADS("PADS"),
        DIAPERS("DIAPERS"),
        OTHER_MOTHER_CHILD("OTHER-MOTHER-CHILD");

        private static final HashMap<String, Subcategory> MAP = new HashMap<>();
        private final String label;

        static {
            for (Subcategory subcategory : Subcategory.values()) {
                MAP.put(subcategory.getLabel(), subcategory);
            }
        }

        Subcategory(String label) {
            this.label = label;
        }

        public static Subcategory fromLabel(String label) {
            return MAP.get(label);
        }
    }
}

