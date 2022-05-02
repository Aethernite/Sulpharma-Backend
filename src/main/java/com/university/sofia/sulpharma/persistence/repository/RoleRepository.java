package com.university.sofia.sulpharma.persistence.repository;


import com.university.sofia.sulpharma.persistence.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This repository is responsible to contain all queries that is related to {@link Role}
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    /**
     * Find by name.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Role> findByName(Role.RoleType name);

    /**
     * Finds all.
     *
     * @return the List of all roles
     */
    List<Role> findAll();
}