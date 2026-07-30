package io.github.artsobol.kurkod.feature.iam.repository;

import io.github.artsobol.kurkod.feature.iam.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(long userId);
}
