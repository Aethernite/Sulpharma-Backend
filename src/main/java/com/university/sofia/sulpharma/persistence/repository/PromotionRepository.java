package com.university.sofia.sulpharma.persistence.repository;

import com.university.sofia.sulpharma.persistence.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Promotion repository.
 */
public interface PromotionRepository extends CrudRepository<Promotion, Long> {
    List<Promotion> findAll();

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Promotion> findAll(Pageable pageable);

    /**
     * Find one by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Promotion> findOneByName(String name);
}
