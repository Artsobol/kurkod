package io.github.artsobol.kurkod.feature.auth.dto.response;

public record AuthResponse(String accessToken, String refreshToken, UserInfo user) {}
