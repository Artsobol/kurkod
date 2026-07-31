package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.iam.entity.RefreshToken;
import io.github.artsobol.kurkod.feature.iam.entity.User;
import io.github.artsobol.kurkod.feature.iam.repository.RefreshTokenRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateOrUpdateRefreshToken(User user) {
        return refreshTokenRepository.findByUserId(user.getId())
                .map(refreshToken -> {
                    refreshToken.setCreatedAt(Instant.now());
                    refreshToken.setToken(generateRefreshToken());
                    return refreshTokenRepository.save(refreshToken);
                })
                .orElseGet(
                        () -> {
                            RefreshToken newToken = new RefreshToken();
                            newToken.setUser(user);
                            newToken.setCreatedAt(Instant.now());
                            newToken.setToken(generateRefreshToken());
                            return refreshTokenRepository.save(newToken);
                        }
                );
    }

    @Override
    public RefreshToken validateAndRefreshToken(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(
                        () -> new NotFoundException("jwt.refresh.token.not.found", requestRefreshToken)
                );

        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setToken(generateRefreshToken());
        return refreshTokenRepository.save(refreshToken);
    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
