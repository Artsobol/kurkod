package io.github.artsobol.kurkod.feature.auth.refreshtoken.service;

import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.RotateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.response.RefreshTokenRotationResponse;

import java.util.UUID;

public interface RefreshTokenService {

    String createRefreshToken(CreateRefreshTokenRequest request);

    RefreshTokenRotationResponse rotate(RotateRefreshTokenRequest request);

    void revoke(String rawRefreshToken);

    boolean isSessionActive(Long userId, UUID sessionId);
}
