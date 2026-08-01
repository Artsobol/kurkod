package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.exception.security.AuthenticationException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.auth.dto.request.LoginRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.user.entity.User;
import io.github.artsobol.kurkod.feature.user.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

  private final UserService userService;
  private final AuthResponseFactory authResponseFactory;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public AuthResponse login(LoginRequest request, SessionMetadata metadata) {
    User user;
    try {
      user = userService.findActiveByEmail(request.getEmail());
    } catch (NotFoundException exception) {
      throw new AuthenticationException("auth.bad-credentials");
    }
    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
      throw new AuthenticationException("auth.bad-credentials");
    }
    UUID sessionId = UUID.randomUUID();
    return authResponseFactory.create(
        new CreateRefreshTokenRequest(
            user,
            sessionId,
            metadata.ipAddress(),
            metadata.userAgent(),
            metadata.deviceName()));
  }
}
