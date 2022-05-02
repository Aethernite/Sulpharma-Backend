package com.university.sofia.sulpharma.persistence.repository;

import com.university.sofia.sulpharma.persistence.entity.Role;
import com.university.sofia.sulpharma.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This repository is responsible to contain all queries that is related to {@link User}
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Find by username.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Exists by username.
     *
     * @param username the username
     * @return true if it exists and false if it doesn't
     */
    Boolean existsByUsername(String username);

    /**
     * Finds all.
     *
     * @return the List of all users
     */
    List<User> findAll();

    /**
     * Finds all users with certain roles.
     *
     * @return the List of all doctors
     */
    Page<User> findByRolesIn(Collection<Role> roles, Pageable pageable);

}