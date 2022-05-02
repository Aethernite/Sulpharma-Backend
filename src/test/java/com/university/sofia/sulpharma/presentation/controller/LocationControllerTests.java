package com.university.sofia.sulpharma.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.university.sofia.sulpharma.persistence.dto.LocationDTO;
import com.university.sofia.sulpharma.persistence.entity.Location;
import com.university.sofia.sulpharma.persistence.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class LocationControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void createNewLocation() throws Exception {
        //given
        var locationDTO = new LocationDTO("Name", "City", "Address", "Description", 42.04, -93.04);

        //when
        mvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllLocations() throws Exception {
        //given
        var locations = Arrays.asList(
                new LocationDTO("Name1", "City1", "Address1", "Description1", 42.04, -93.04),
                new LocationDTO("Name2", "City2", "Address2", "Description2", 45.04, -93.04)
        );

        locations.forEach(locationDTO -> locationRepository.save(modelMapper.map(locationDTO, Location.class)));

        // when
        var response = mvc.perform(get("/api/v1/locations/?page=0"))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void updateLocationById() throws Exception {
        //given
        var locationInDatabase = new LocationDTO("Name1", "City1", "Address1", "Description1", 42.04, -93.04);
        locationRepository.save(modelMapper.map(locationInDatabase, Location.class));

        var newLocationInformation = new LocationDTO("Name2", "City2", "Address2", "Description2", 45.04, -93.04);

        var locationInDatabaseWithId = locationRepository.findOneByName("Name1")
                .orElse(null);
        //when
        assert locationInDatabaseWithId!=null;

        var response = mvc.perform(put("/api/v1/locations/{locationId}", locationInDatabaseWithId.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newLocationInformation)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void getLocationById() throws Exception {
        //given
        var locationInDatabase = new LocationDTO("Name1", "City1", "Address1", "Description1", 42.04, -93.04);
        var location = modelMapper.map(locationInDatabase, Location.class);
        locationRepository.save(location);

        var locationInDatabaseWithId = locationRepository.findOneByName("Name1")
                .orElse(null);

        //when
        assert locationInDatabaseWithId!=null;
        var response = mvc.perform(get("/api/v1/locations/{locationId}", locationInDatabaseWithId.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //then
        var locationResponse = objectMapper.readValue(response.getResponse().getContentAsString(), LocationDTO.class);
        locationResponse.setId(null);
        assertThat(locationResponse, equalTo(locationInDatabase));
    }

    @Test
    void getLocationByIdThatNotExist() throws Exception {
        //when
        long invalidId = 10000000L;
        var response = mvc.perform(delete("/api/v1/locations/{locationId}", invalidId))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponse().getStatus());
    }

    @Test
    void deleteLocationById() throws Exception {
        //given
        var locationInDatabase = new LocationDTO("Name1", "City1", "Address1", "Description1", 42.04, -93.04);
        var location = modelMapper.map(locationInDatabase, Location.class);
        locationRepository.save(location);

        var locationInDatabaseWithId = locationRepository.findOneByName("Name1")
                .orElse(null);
        //when
        assert locationInDatabaseWithId!=null;
        var response = mvc.perform(delete("/api/v1/locations/{locationId}", locationInDatabaseWithId.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertTrue(locationRepository.findById(locationInDatabaseWithId.getId()).isEmpty());
        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

}
