package com.university.sofia.sulpharma.persistence.repository;

import com.university.sofia.sulpharma.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Product repository.
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAll();

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Product> findAll(Pageable pageable);

    /**
     * Find all by description containing page.
     *
     * @param pageable the pageable
     * @param query    the query
     * @return the page
     */
    Page<Product> findAllByDescriptionContaining(Pageable pageable, String query);

    /**
     * Find all by category page.
     *
     * @param pageable the pageable
     * @param category the category
     * @return the page
     */
    Page<Product> findAllByCategory(Pageable pageable, Product.Category category);

    /**
     * Find all by category and subcategory page.
     *
     * @param pageable    the pageable
     * @param category    the category
     * @param subcategory the subcategory
     * @return the page
     */
    Page<Product> findAllByCategoryAndSubcategory(Pageable pageable, Product.Category category, Product.Subcategory subcategory);

    /**
     * Find one by description optional.
     *
     * @param description the description
     * @return the optional
     */
    Optional<Product> findOneByDescription(String description);


}
