package com.university.sofia.sulpharma.business;

import com.university.sofia.sulpharma.persistence.dto.JwtResponseDTO;
import com.university.sofia.sulpharma.persistence.dto.LoginRequestDTO;
import com.university.sofia.sulpharma.persistence.dto.RegisterRequestDTO;
import com.university.sofia.sulpharma.persistence.dto.RoleDTO;
import com.university.sofia.sulpharma.persistence.entity.RefreshToken;
import com.university.sofia.sulpharma.persistence.entity.Role;
import com.university.sofia.sulpharma.persistence.entity.User;
import com.university.sofia.sulpharma.persistence.repository.RefreshTokenRepository;
import com.university.sofia.sulpharma.persistence.repository.RoleRepository;
import com.university.sofia.sulpharma.persistence.repository.UserRepository;
import com.university.sofia.sulpharma.presentation.exception.TokenRefreshException;
import com.university.sofia.sulpharma.presentation.exception.UserExistsException;
import com.university.sofia.sulpharma.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The Authentication service.
 * <p>
 * This service takes care of the authentication of a certain user
 */
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @Value("${app.jwtRefreshExpiration}")
    private Long refreshTokenDurationMs;

    /**
     * Sign-up.
     *
     * @param createUserDto the create user dto
     */
    public void register(RegisterRequestDTO createUserDto) {
        Set<RoleDTO> userRoles = createUserDto.getRoles();
        if (userRepository.findByUsername(createUserDto.getUsername()).isPresent()) {
            throw new UserExistsException("User with the given email already exists.");
        }
        Set<Role> mappedRoles = new HashSet<>();
        userRoles.forEach(role -> mappedRoles
                .add(roleRepository.findByName(role.getName())
                        .orElseThrow(RuntimeException::new)));

        User user = new User(
                createUserDto.getUsername(),
                passwordEncoder.encode(createUserDto.getPassword()),
                mappedRoles
        );

        userRepository.save(user);
    }

    /**
     * Sign-in.
     *
     * @param loginRequestDTO the login request dto
     * @return the jwt response dto
     */
    public JwtResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        User user = (User) authentication.getPrincipal();
        RefreshToken refreshToken = createRefreshToken(user.getId());

        return new JwtResponseDTO(authentication.getName(), jwt, refreshToken.getToken());
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Used for user logout.
     *
     * @return {@link Map} with key message and value contains information about successful logout
     */
    public Map<String, String> logout() {
        SecurityContextHolder.clearContext();
        return Map.of("message", "logout successfully");
    }

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Create refresh token.
     *
     * @param userId the user id
     * @return the refresh token
     */
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * Verify expiration refresh token.
     *
     * @param token the token
     * @return the refresh token
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin " +
                    "request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        return refreshTokenRepository.deleteByUser(user);
    }
}