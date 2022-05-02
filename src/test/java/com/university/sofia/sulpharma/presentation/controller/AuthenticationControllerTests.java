package com.university.sofia.sulpharma.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.university.sofia.sulpharma.persistence.dto.LoginRequestDTO;
import com.university.sofia.sulpharma.persistence.dto.RegisterRequestDTO;
import com.university.sofia.sulpharma.persistence.dto.RoleDTO;
import com.university.sofia.sulpharma.persistence.entity.Role;
import com.university.sofia.sulpharma.persistence.entity.User;
import com.university.sofia.sulpharma.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AuthenticationControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerValidUser() throws Exception {
        //given
        Set<RoleDTO> rolesToAdd = new HashSet<>();
        RoleDTO role = new RoleDTO();
        role.setName(Role.RoleType.CUSTOMER);
        rolesToAdd.add(role);
        var userDTO = new RegisterRequestDTO("borislav@mail.com", "password4e", rolesToAdd);

        //when
        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());

        User user = userRepository.findByUsername("borislav@mail.com").orElse(null);

        assert user!=null;
        Assertions.assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void registerInvalidUser() throws Exception {
        //given
        var userDTO = new RegisterRequestDTO("", "", null);

        //when
        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void loginValidUser() throws Exception {
        //given
        Set<RoleDTO> rolesToAdd = new HashSet<>();
        RoleDTO role = new RoleDTO();
        role.setName(Role.RoleType.CUSTOMER);
        rolesToAdd.add(role);
        var registerDTO = new RegisterRequestDTO("borislav@mail.com", "password4e", rolesToAdd);
        var loginDTO = new LoginRequestDTO("borislav@mail.com", "password4e");
        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());

        //when
        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());

    }

    @Test
    void loginInvalidUser() throws Exception {
        //given
        var loginDTO = new LoginRequestDTO("invaliduser@mail.bg", "password4e");
        //when

        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());

    }

}
