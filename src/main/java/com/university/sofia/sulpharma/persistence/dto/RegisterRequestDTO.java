package com.university.sofia.sulpharma.persistence.dto;

import com.university.sofia.sulpharma.persistence.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * The type Signup request dto.
 * <p>
 * Contains the user information needed for the sign-up
 */
@Schema(description = "Signup Request DTO is a DTO that is used for the transferring the username, email, password " +
        "and the roles for the sign-up operation", allowableValues = {"username", "email", "password", "roles"})
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Validated
public class RegisterRequestDTO {
    @Schema(description = "The username of the client")
    @Size(max = 20, min = 5)
    @NotBlank
    @Email
    private String username;

    @Schema(description = "The password of the client")
    @NotBlank
    @Size(max = 20, min = 5)
    private String password;

    @Schema(description = "The roles of the client's account")
    @NotNull
    private Set<@Valid RoleDTO> roles = Set.of(new RoleDTO(Role.RoleType.CUSTOMER));
}
