package io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request;

import io.github.artsobol.kurkod.feature.user.entity.User;

import java.util.UUID;

public record CreateRefreshTokenRequest(
        User user,
        UUID sessionId,
        String ipAddress,
        String userAgent,
        String deviceName
) {
}
