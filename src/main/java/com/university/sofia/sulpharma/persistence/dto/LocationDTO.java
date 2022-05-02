package com.university.sofia.sulpharma.persistence.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Location DTO
 * <p>
 * Contains the needed information for a single Location
 */
@Data
@NoArgsConstructor
public class LocationDTO {
    private Long id;
    private String name;
    private String city;
    private String address;
    private String description;
    private Double latitude;
    private Double longitude;

    public LocationDTO(
            String name,
            String city,
            String address,
            String description,
            Double latitude,
            Double longitude
    ) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
