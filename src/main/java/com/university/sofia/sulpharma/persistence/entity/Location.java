package com.university.sofia.sulpharma.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Location extends BaseEntity {
    @NotEmpty
    private String name;
    @NotEmpty
    private String address;
    @NotEmpty
    private String city;
    @NotEmpty
    private String description;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    public void update(Location location) {
        this.name = location.getName();
        this.description = location.getDescription();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.city = location.getCity();
        this.address = location.getAddress();
    }
}
