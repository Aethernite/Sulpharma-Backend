package com.university.sofia.sulpharma.persistence.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JwtRefreshRequestDTO {

    @NotBlank
    private String refreshToken;

}
