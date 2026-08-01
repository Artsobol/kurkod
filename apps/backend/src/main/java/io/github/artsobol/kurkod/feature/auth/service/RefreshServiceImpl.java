package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.RotateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.response.RefreshTokenRotationResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

  private final RefreshTokenService refreshTokenService;
  private final AuthResponseFactory authResponseFactory;

  @Override
  @Transactional
  public AuthResponse refresh(RotateRefreshTokenRequest request) {
    RefreshTokenRotationResponse rotated = refreshTokenService.rotate(request);
    return authResponseFactory.createWithRefresh(
        rotated.user(), rotated.rawRefreshToken(), rotated.sessionId());
  }

  @Override
  @Transactional
  public void logout(String rawRefreshToken) {
    refreshTokenService.revoke(rawRefreshToken);
  }
}
