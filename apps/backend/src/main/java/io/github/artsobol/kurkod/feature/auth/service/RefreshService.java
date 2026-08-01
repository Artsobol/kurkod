package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.RotateRefreshTokenRequest;

public interface RefreshService {
  AuthResponse refresh(RotateRefreshTokenRequest request);

  void logout(String rawRefreshToken);
}
