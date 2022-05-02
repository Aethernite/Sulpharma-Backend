package com.university.sofia.sulpharma.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.university.sofia.sulpharma.persistence.dto.PromotionDTO;
import com.university.sofia.sulpharma.persistence.entity.Promotion;
import com.university.sofia.sulpharma.persistence.repository.PromotionRepository;
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

import java.time.LocalDate;
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
class PromotionControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PromotionRepository promotionRepository;

    @Test
    void createNewPromotion() throws Exception {
        //given
        var promotionDTO = new PromotionDTO("Name", "url", LocalDate.now(), LocalDate.now());

        //when
        mvc.perform(post("/api/v1/promotions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promotionDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllPromotions() throws Exception {
        //given
        var promotions = Arrays.asList(
                new PromotionDTO("Name", "url", LocalDate.now(), LocalDate.now()),
                new PromotionDTO("Name2", "url2", LocalDate.now(), LocalDate.now())
        );

        promotions.forEach(promotionDTO -> promotionRepository.save(modelMapper.map(promotionDTO, Promotion.class)));

        // when
        var response = mvc.perform(get("/api/v1/promotions/?page=0"))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void updatePromotionById() throws Exception {
        //given
        var promotionInDatabase = new PromotionDTO("Name1", "url", LocalDate.now(), LocalDate.now());
        promotionRepository.save(modelMapper.map(promotionInDatabase, Promotion.class));

        var newPromotionInformation = new PromotionDTO("Name2", "url2", LocalDate.now(), LocalDate.now());

        var promotionInDatabaseWithId = promotionRepository.findOneByName("Name1")
                .orElse(null);
        //when
        assert promotionInDatabaseWithId!=null;

        var response = mvc.perform(put("/api/v1/promotions/{promotionId}", promotionInDatabaseWithId.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newPromotionInformation)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void getPromotionById() throws Exception {
        //given
        var promotionInDatabase = new PromotionDTO("Name1", "url", LocalDate.now(), LocalDate.now());
        var promotion = modelMapper.map(promotionInDatabase, Promotion.class);
        promotionRepository.save(promotion);

        var promotionInDatabaseWithId = promotionRepository.findOneByName("Name1")
                .orElse(null);

        //when
        assert promotionInDatabaseWithId!=null;
        var response = mvc.perform(get("/api/v1/promotions/{promotionId}", promotionInDatabaseWithId.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //then
        var promotionResponse = objectMapper.readValue(response.getResponse().getContentAsString(), PromotionDTO.class);
        promotionResponse.setId(null);
        assertThat(promotionResponse, equalTo(promotionInDatabase));
    }

    @Test
    void getPromotionByIdThatNotExist() throws Exception {
        //when
        long invalidId = 10000000L;
        var response = mvc.perform(delete("/api/v1/promotions/{promotionId}", invalidId))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponse().getStatus());
    }

    @Test
    void deletePromotionById() throws Exception {
        //given
        var promotionInDatabase = new PromotionDTO("Name1", "url", LocalDate.now(), LocalDate.now());
        var promotion = modelMapper.map(promotionInDatabase, Promotion.class);
        promotionRepository.save(promotion);

        var promotionInDatabaseWithId = promotionRepository.findOneByName("Name1")
                .orElse(null);
        //when
        assert promotionInDatabaseWithId!=null;
        var response = mvc.perform(delete("/api/v1/promotions/{promotionId}", promotionInDatabaseWithId.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertTrue(promotionRepository.findById(promotionInDatabaseWithId.getId()).isEmpty());
        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

}
