package io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.response;

import io.github.artsobol.kurkod.feature.auth.refreshtoken.service.GeneratedRefreshToken;

public record RefreshTokenResponse(String rawToken, GeneratedRefreshToken refreshToken) {}
