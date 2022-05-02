package com.university.sofia.sulpharma.persistence.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The abstract class Base entity.
 * <p>
 * The Base entity is used for all of the entities.
 * Main purpose of the class is to introduce the field ID for the database persistence
 */
@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}