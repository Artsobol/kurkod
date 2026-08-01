package io.github.artsobol.kurkod.feature.auth.refreshtoken.service;

import io.github.artsobol.kurkod.config.security.RefreshTokenProperties;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.response.RefreshTokenResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.entity.RefreshToken;
import io.github.artsobol.kurkod.infrastructure.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenEncoder {

    private final RefreshTokenProperties properties;

    public RefreshTokenResponse create(CreateRefreshTokenRequest request) {

        String rawToken = TokenUtils.generateRawToken(properties.length());
        RefreshToken token = RefreshToken.create(request, hash(rawToken), calculateExpiresAt());

        return new RefreshTokenResponse(rawToken, token);
    }

    public String hash(String rawToken) {
        return TokenUtils.hmacSha256Base64Url(rawToken, properties.pepper());
    }

    private Instant calculateExpiresAt() {
        return Instant.now().plus(properties.ttl());
    }
}
