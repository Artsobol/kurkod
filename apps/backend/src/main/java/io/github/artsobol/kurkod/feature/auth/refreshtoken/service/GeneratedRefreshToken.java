package io.github.artsobol.kurkod.feature.auth.refreshtoken.service;

import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record GeneratedRefreshToken(String rawToken, String tokenHash, Instant expiresAt) {

  public GeneratedRefreshToken {
    Objects.requireNonNull(rawToken, "Raw token must not be null");
    Objects.requireNonNull(tokenHash, "Token hash must not be null");
    Objects.requireNonNull(expiresAt, "Expiration must not be null");
  }

  @NonNull @Override
  public String toString() {
    return "GeneratedRefreshToken[rawToken=<redacted>, tokenHash=<redacted>, expiresAt="
        + expiresAt
        + "]";
  }
}
