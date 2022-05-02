package com.university.sofia.sulpharma.persistence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Jwt response dto.
 * <p>
 * Contains the jwt token
 */
@Schema(description = "JWT Response DTO is a DTO that is used for transferring the JWT token", allowableValues = {
        "token"})
@AllArgsConstructor
@Data
public class JwtResponseDTO {
    @Schema(description = "The user's email")
    private String user;
    @Schema(description = "The JWT token")
    private String token;
    @Schema(description = "The JWT refresh token")
    private String refreshToken;
}
