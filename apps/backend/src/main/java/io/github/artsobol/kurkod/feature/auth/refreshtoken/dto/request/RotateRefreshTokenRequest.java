package io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request;

public record RotateRefreshTokenRequest(
        String rawRefreshToken,
        String ipAddress,
        String userAgent
) {
}
