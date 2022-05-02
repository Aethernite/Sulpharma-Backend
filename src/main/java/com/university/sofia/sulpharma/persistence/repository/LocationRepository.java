package com.university.sofia.sulpharma.persistence.repository;

import com.university.sofia.sulpharma.persistence.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Location repository.
 */
public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findAll();

    /**
     * Find all paged.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Location> findAll(Pageable pageable);

    /**
     * Find one by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Location> findOneByName(String name);
}

