package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.kurkod.feature.auth.dto.response.UserInfo;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.service.RefreshTokenService;
import io.github.artsobol.kurkod.feature.user.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthResponseFactory {

  private final AccessTokenService accessTokenService;
  private final RefreshTokenService refreshTokenService;

  public AuthResponse create(CreateRefreshTokenRequest request) {
    User user = request.user();
    return createWithRefresh(
        user, refreshTokenService.createRefreshToken(request), request.sessionId());
  }

  public AuthResponse createWithRefresh(User user, String rawRefreshToken, UUID sessionId) {
    return new AuthResponse(
        accessTokenService.createAccessToken(user, sessionId),
        rawRefreshToken,
        new UserInfo(user.getId(), user.getUsername(), user.getRole()));
  }
}
