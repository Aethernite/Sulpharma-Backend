package com.university.sofia.sulpharma.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.sofia.sulpharma.persistence.dto.ProductDTO;
import com.university.sofia.sulpharma.persistence.entity.Product;
import com.university.sofia.sulpharma.persistence.repository.ProductRepository;
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

import java.math.BigDecimal;
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
class ProductControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createNewProduct() throws Exception {
        //given
        var productDTO = new ProductDTO("ImageUrl", "Description", BigDecimal.valueOf(20), true, 5, Product.Category.HEALTH, Product.Subcategory.ALERGY, false, BigDecimal.valueOf(20));

        //when
        mvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllProducts() throws Exception {
        //given
        var products = Arrays.asList(
                new ProductDTO("ImageUrl1", "Description1", BigDecimal.valueOf(20), true, 5, Product.Category.HEALTH,
                        Product.Subcategory.ALERGY,false, BigDecimal.valueOf(20)),
                new ProductDTO("ImageUrl2", "Description2", BigDecimal.valueOf(20), true, 5, Product.Category.HEALTH,
                        Product.Subcategory.ALERGY, false, BigDecimal.valueOf(20))
        );

        products.forEach(productDTO -> productRepository.save(modelMapper.map(productDTO, Product.class)));

        // when
        var response = mvc.perform(get("/api/v1/products/?page=0")
                .param("sort", "description")
                .param("order", "ASC"))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void updateProductById() throws Exception {
        //given
        var productInDatabase = new ProductDTO("ImageUrl", "Description", BigDecimal.valueOf(20), true, 5,
                Product.Category.HEALTH, Product.Subcategory.ALERGY, false, BigDecimal.valueOf(20));
        productRepository.save(modelMapper.map(productInDatabase, Product.class));

        var newProductInformation = new ProductDTO("ImageUrl2", "Description2", BigDecimal.valueOf(20), true, 5,
                Product.Category.HEALTH, Product.Subcategory.ALERGY, false, BigDecimal.valueOf(20));

        var productInDatabaseWithId = productRepository.findOneByDescription("Description")
                .orElse(null);
        //when
        assert productInDatabaseWithId != null;

        var response = mvc.perform(put("/api/v1/products/{productId}", productInDatabaseWithId.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newProductInformation)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void getProductById() throws Exception {
        //given
        var productInDatabase = new ProductDTO("ImageUrl", "Description", BigDecimal.valueOf(20), true, 5,
                Product.Category.HEALTH, Product.Subcategory.ALERGY, false, BigDecimal.valueOf(20));
        var product = modelMapper.map(productInDatabase, Product.class);
        productRepository.save(product);

        var productInDatabaseWithId = productRepository.findOneByDescription("Description")
                .orElse(null);

        //when
        assert productInDatabaseWithId != null;
        var response = mvc.perform(get("/api/v1/products/{productId}", productInDatabaseWithId.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //then
        var productResponse = objectMapper.readValue(response.getResponse().getContentAsString(), ProductDTO.class);
        productResponse.setId(null);
        assertThat(productResponse, equalTo(productInDatabase));
    }

    @Test
    void getProductByIdThatNotExist() throws Exception {
        //when
        long invalidId = 10000000L;
        var response = mvc.perform(delete("/api/v1/products/{productId}", invalidId))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponse().getStatus());
    }

    @Test
    void deleteProductById() throws Exception {
        //given
        var productInDatabase = new ProductDTO("ImageUrl", "Description", BigDecimal.valueOf(20), true, 5,
                Product.Category.HEALTH, Product.Subcategory.ALERGY, false, BigDecimal.valueOf(20));
        var product = modelMapper.map(productInDatabase, Product.class);
        productRepository.save(product);

        var productInDatabaseWithId = productRepository.findOneByDescription("Description")
                .orElse(null);
        //when
        assert productInDatabaseWithId != null;
        var response = mvc.perform(delete("/api/v1/products/{productId}", productInDatabaseWithId.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertTrue(productRepository.findById(productInDatabaseWithId.getId()).isEmpty());
        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

}
