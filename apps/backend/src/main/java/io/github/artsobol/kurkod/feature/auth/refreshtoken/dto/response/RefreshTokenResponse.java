package io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.response;

import io.github.artsobol.kurkod.feature.auth.refreshtoken.entity.RefreshToken;

public record RefreshTokenResponse(
        String rawToken,
        RefreshToken refreshToken
) {
}
