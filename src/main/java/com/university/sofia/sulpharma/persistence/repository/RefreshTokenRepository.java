package com.university.sofia.sulpharma.persistence.repository;

import com.university.sofia.sulpharma.persistence.entity.RefreshToken;
import com.university.sofia.sulpharma.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Refresh token repository.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Delete by user int.
     *
     * @param user the user
     * @return the int
     */
    int deleteByUser(User user);

}
