package com.university.sofia.sulpharma.presentation.controller;

import com.university.sofia.sulpharma.business.LocationService;
import com.university.sofia.sulpharma.persistence.dto.LocationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/locations")
@CrossOrigin
public class LocationController {

    private final LocationService locationService;

    /**
     * Create location response entity.
     *
     * @param locationDTO the location dto
     * @return the response entity
     */
    @Operation(summary = "This request method is used for creating new location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location is created successfully"),
            @ApiResponse(responseCode = "400", description = "The request body is not correct"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationDTO locationDTO) {
        return new ResponseEntity<>(locationService.createLocation(locationDTO), HttpStatus.CREATED);
    }

    /**
     * Get all response entity.
     *
     * @return the response entity
     */
    @Operation(summary = "This request method return all of our locations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return page of locations"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping
    public ResponseEntity<List<LocationDTO>> findAll() {
        return new ResponseEntity<>(locationService.findAll(), HttpStatus.OK);
    }

    /**
     * Gets location by id.
     *
     * @param locationId the location id
     * @return the location by id
     */
    @Operation(summary = "This request method return location by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return location"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable("locationId") @Min(value = 1) Long locationId) {
        return new ResponseEntity<>(locationService.getLocationById(locationId), HttpStatus.OK);
    }

    /**
     * Update location by id response entity.
     *
     * @param locationId  the location id
     * @param locationDTO the location dto
     * @return the response entity
     */
    @Operation(summary = "This request method update location by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return updated location"),
            @ApiResponse(responseCode = "400", description = "The request body is not correct"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDTO> updateLocationById(@PathVariable("locationId") @Min(value = 1) @NotNull Long locationId, @Valid @RequestBody LocationDTO locationDTO) {
        return new ResponseEntity<>(locationService.updateLocationById(locationId, locationDTO), HttpStatus.OK);
    }

    /**
     * Delete location by id response entity.
     *
     * @param locationId the location id
     * @return the response entity
     */
    @Operation(summary = "This request method delete location by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted location"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDTO> deleteLocationById(@PathVariable("locationId") @Min(value = 1) @NotNull Long locationId) {
        return new ResponseEntity<>(locationService.deleteLocationById(locationId), HttpStatus.OK);
    }

}

