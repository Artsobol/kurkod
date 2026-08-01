package io.github.artsobol.kurkod.feature.auth.refreshtoken.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.artsobol.kurkod.config.security.SessionProperties;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.RotateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.response.RefreshTokenResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.response.RefreshTokenRotationResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.entity.RefreshToken;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.repository.RefreshTokenRepository;
import io.github.artsobol.kurkod.feature.user.entity.User;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

  @Mock private RefreshTokenRepository repository;
  @Mock private RefreshTokenEncoder encoder;
  private RefreshTokenServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new RefreshTokenServiceImpl(repository, encoder, new SessionProperties(5));
  }

  @Test
  void rotateRevokesOldTokenAndReturnsReplacement() {
    User user = User.create("alice", "alice@example.com", "hash");
    UUID sessionId = UUID.randomUUID();
    CreateRefreshTokenRequest oldRequest =
        new CreateRefreshTokenRequest(user, sessionId, "old-ip", "old-agent", "device");
    RefreshToken oldToken = RefreshToken.create(oldRequest, "old-hash", Instant.now().plusSeconds(60));
    CreateRefreshTokenRequest newRequest =
        new CreateRefreshTokenRequest(user, sessionId, "new-ip", "new-agent", "device");
    RefreshToken newToken = RefreshToken.create(newRequest, "new-hash", Instant.now().plusSeconds(60));
    when(encoder.hash("raw-old")).thenReturn("old-hash");
    when(repository.findByTokenHash("old-hash")).thenReturn(Optional.of(oldToken));
    when(encoder.create(newRequest)).thenReturn(new RefreshTokenResponse("raw-new", newToken));

    RefreshTokenRotationResponse response =
        service.rotate(new RotateRefreshTokenRequest("raw-old", "new-ip", "new-agent"));

    assertThat(oldToken.isRevoked()).isTrue();
    assertThat(response.rawRefreshToken()).isEqualTo("raw-new");
    assertThat(response.sessionId()).isEqualTo(sessionId);
    verify(repository).save(newToken);
  }
}
