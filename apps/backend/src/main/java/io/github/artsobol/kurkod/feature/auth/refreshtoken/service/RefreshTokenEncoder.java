package io.github.artsobol.kurkod.feature.auth.refreshtoken.service;

import io.github.artsobol.kurkod.config.security.RefreshTokenProperties;
import io.github.artsobol.kurkod.infrastructure.utils.TokenUtils;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenEncoder {

  private final RefreshTokenProperties properties;

  public GeneratedRefreshToken generate() {
    String rawToken = TokenUtils.generateRawToken(properties.length());

    return new GeneratedRefreshToken(rawToken, hash(rawToken), calculateExpiresAt());
  }

  public String hash(String rawToken) {
    return TokenUtils.hmacSha256Base64Url(rawToken, properties.pepper());
  }

  private Instant calculateExpiresAt() {
    return Instant.now().plus(properties.ttl());
  }
}
