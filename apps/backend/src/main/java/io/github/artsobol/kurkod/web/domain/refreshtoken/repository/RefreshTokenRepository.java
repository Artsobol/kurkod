package io.github.artsobol.kurkod.web.domain.refreshtoken.repository;

import io.github.artsobol.kurkod.web.domain.refreshtoken.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(int userId);
}
