package com.university.sofia.sulpharma.persistence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * The Login request dto.
 * <p>
 * Contains the user information needed for login
 */
@Schema(description = "Login Request DTO is a DTO that is used for the transferring the username and password for the" +
        " sign-in operation", allowableValues = {"username", "password"})
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequestDTO {

    @Schema(description = "The username of the client")
    @NotBlank
    @Email
    private String username;

    @Schema(description = "The password of the client")
    @NotBlank
    private String password;
}