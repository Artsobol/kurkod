package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.feature.auth.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.user.dto.request.CreateUserRequest;
import io.github.artsobol.kurkod.feature.user.entity.User;
import io.github.artsobol.kurkod.feature.user.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

  private final PasswordEncoder passwordEncoder;
  private final AuthResponseFactory authResponseFactory;
  private final UserService userService;

  @Override
  @Transactional
  public AuthResponse register(RegistrationRequest request, SessionMetadata metadata) {
    User user = userService.createUser(
        new CreateUserRequest(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())));
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
