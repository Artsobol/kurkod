package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.feature.user.entity.User;
import java.util.UUID;

public interface AccessTokenService {
  String createAccessToken(User user, UUID sessionId);
}
