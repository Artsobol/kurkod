package io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.response;

import io.github.artsobol.kurkod.feature.user.entity.User;

import java.util.UUID;

public record RefreshTokenRotationResponse(
        User user,
        String rawRefreshToken,
        UUID sessionId
) {
}
