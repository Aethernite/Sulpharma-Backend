package com.university.sofia.sulpharma.persistence;

import com.opencsv.CSVReader;
import com.university.sofia.sulpharma.persistence.entity.Location;
import com.university.sofia.sulpharma.persistence.entity.Product;
import com.university.sofia.sulpharma.persistence.entity.Promotion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Csv file parser.
 */
@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvFileParser {
    private static final String PRODUCTS_PATH = "./data/products/";
    private static final String PROMOTIONS_PATH = "./data/promotions/";
    private static final String LOCATIONS_PATH = "./data/locations/";

    /**
     * Fetch products list from csv.
     *
     * @return the list
     */
    public static List<Product> fetchProducts() {
        FileWalker fileWalker = new FileWalker();

        List<File> csvFiles = fileWalker.walk(PRODUCTS_PATH);

        List<Product> products = new ArrayList<>();

        log.info(String.format("Found %d csv files", csvFiles.size()));

        for (File csvFile : csvFiles) {
            try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                String[] line;

                while ((line = reader.readNext()) != null) {
                    Product product = mapRowToProduct(line);
                    products.add(product);
                }
            } catch (Exception ignored) {
            }
        }

        return products;
    }

    /**
     * Fetch promotions list from csv.
     *
     * @return the list
     */
    public static List<Promotion> fetchPromotions() {
        FileWalker fileWalker = new FileWalker();

        List<File> csvFiles = fileWalker.walk(PROMOTIONS_PATH);

        List<Promotion> promotions = new ArrayList<>();

        log.info(String.format("Found %d csv files", csvFiles.size()));

        for (File csvFile : csvFiles) {
            try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                String[] line;

                while ((line = reader.readNext()) != null) {
                    Promotion promotion = mapRowToPromotion(line);
                    promotions.add(promotion);
                }
            } catch (Exception ignored) {
            }
        }

        return promotions;
    }

    /**
     * Fetch locations list from csv.
     *
     * @return the list
     */
    public static List<Location> fetchLocations() {
        FileWalker fileWalker = new FileWalker();

        List<File> csvFiles = fileWalker.walk(LOCATIONS_PATH);

        List<Location> locations = new ArrayList<>();

        log.info(String.format("Found %d csv files", csvFiles.size()));

        for (File csvFile : csvFiles) {
            try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                String[] line;

                while ((line = reader.readNext()) != null) {
                    Location location = mapRowToLocation(line);
                    locations.add(location);
                }
            } catch (Exception ignored) {
            }
        }

        return locations;
    }

    private static Product mapRowToProduct(String[] fields) {
        Product product = new Product();
        product.setDescription(fields[0]);
        product.setPrice(new BigDecimal(fields[1]));
        product.setImageUrl(fields[2]);
        product.setDisplay(true);
        product.setRating(Double.parseDouble(fields[3]));
        product.setCategory(Product.Category.fromLabel(fields[4]));
        product.setSubcategory(Product.Subcategory.fromLabel(fields[5]));
        product.setPromoted(Boolean.parseBoolean(fields[6]));
        product.setPromotionPrice(new BigDecimal(fields[7]));

        return product;
    }

    private static Promotion mapRowToPromotion(String[] fields) {
        Promotion promotion = new Promotion();
        promotion.setName(fields[0]);
        promotion.setImageUrl(fields[1]);
        promotion.setEffectiveDate(LocalDate.parse(fields[2]));
        promotion.setTermDate(LocalDate.parse(fields[3]));

        return promotion;
    }

    private static Location mapRowToLocation(String[] fields) {
        Location location = new Location();

        location.setName(fields[0]);
        location.setAddress(fields[1]);
        location.setCity(fields[2]);
        location.setLongitude(Double.parseDouble(fields[3]));
        location.setLatitude(Double.parseDouble(fields[4]));
        location.setDescription(fields[5].replace("#", "\n"));

        return location;
    }


}
