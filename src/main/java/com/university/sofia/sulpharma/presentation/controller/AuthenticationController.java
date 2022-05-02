package com.university.sofia.sulpharma.presentation.controller;

import com.university.sofia.sulpharma.business.AuthenticationService;
import com.university.sofia.sulpharma.persistence.dto.JwtRefreshRequestDTO;
import com.university.sofia.sulpharma.persistence.dto.JwtResponseDTO;
import com.university.sofia.sulpharma.persistence.dto.LoginRequestDTO;
import com.university.sofia.sulpharma.persistence.dto.RegisterRequestDTO;
import com.university.sofia.sulpharma.persistence.entity.RefreshToken;
import com.university.sofia.sulpharma.presentation.exception.TokenRefreshException;
import com.university.sofia.sulpharma.security.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * The Authentication controller.
 * <p>
 * Takes care of the sign-up and sign-in processes
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtUtils jwtUtils;

    /**
     * Tries to sign-up a given valid user given by the request body
     *
     * @param createUserDto the create user dto
     * @return the response entity
     */
    @Operation(summary = "This request method signs-up an account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully signed up an account"),
            @ApiResponse(responseCode = "400", description = "The request body is not correct"),
            @ApiResponse(responseCode = "409", description = "The username already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDTO createUserDto) {
        authenticationService.register(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Tries to sign-in a given valid user given by the request body.
     *
     * @param loginRequestDTO the login request dto
     * @return the jwt response dto
     */
    @Operation(summary = "This request method signs-in an account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully signed in with the account"),
            @ApiResponse(responseCode = "400", description = "The request body is not correct"),
            @ApiResponse(responseCode = "401", description = "Unsuccessful login/Account doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(authenticationService.login(loginRequestDTO), HttpStatus.OK);
    }

    /**
     * Current user name response entity.
     *
     * @param authentication the authentication
     * @return the response entity
     */
    @Operation(summary = "Gets logged user", description = "This request method gets logged user.", tags = {"Get Me"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get user information successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/me")
    public ResponseEntity<String> currentUserName(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }

    /**
     * Logout response entity.
     *
     * @return the response entity
     */
    @Operation(summary = "logout", description = "This request method logout user.", tags = {"Authentication"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is logout successfully"),
            @ApiResponse(responseCode = "403", description = "Operation is forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(authenticationService.logout());
    }

    /**
     * Refresh token response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponseDTO> refreshToken(@Valid @RequestBody JwtRefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        return authenticationService.findByToken(requestRefreshToken)
                .map(authenticationService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new JwtResponseDTO(user.getUsername(), token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

}
