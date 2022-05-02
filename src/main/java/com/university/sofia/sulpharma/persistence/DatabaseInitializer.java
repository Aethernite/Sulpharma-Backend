package com.university.sofia.sulpharma.persistence;

import com.university.sofia.sulpharma.persistence.entity.Location;
import com.university.sofia.sulpharma.persistence.entity.Product;
import com.university.sofia.sulpharma.persistence.entity.Promotion;
import com.university.sofia.sulpharma.persistence.entity.Role;
import com.university.sofia.sulpharma.persistence.repository.LocationRepository;
import com.university.sofia.sulpharma.persistence.repository.ProductRepository;
import com.university.sofia.sulpharma.persistence.repository.PromotionRepository;
import com.university.sofia.sulpharma.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The Database initializer.
 * <p>
 * Used to initialize the database with the user roles if they don't exist already
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DatabaseInitializer implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final LocationRepository locationRepository;

    @Override
    public void run(ApplicationArguments args) {
        initialize();
    }

    private void initialize() {
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.save(new Role(Role.RoleType.CUSTOMER));
            roleRepository.save(new Role(Role.RoleType.ADMIN));
        }

        if (productRepository.findAll().isEmpty()) {
            List<Product> products = CsvFileParser.fetchProducts();

            log.info(String.format("Fetched %d products", products.size()));

            productRepository.saveAll(products);
        }

        if (promotionRepository.findAll().isEmpty()) {
            List<Promotion> promotions = CsvFileParser.fetchPromotions();

            log.info(String.format("Fetched %d promotions", promotions.size()));

            promotionRepository.saveAll(promotions);
        }

        if (locationRepository.findAll().isEmpty()) {
            List<Location> locations = CsvFileParser.fetchLocations();

            log.info(String.format("Fetched %d locations", locations.size()));

            locationRepository.saveAll(locations);
        }
    }

}
