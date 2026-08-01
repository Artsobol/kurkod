package io.github.artsobol.kurkod.feature.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.artsobol.kurkod.feature.auth.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.dto.request.CreateRefreshTokenRequest;
import io.github.artsobol.kurkod.feature.user.dto.request.CreateUserRequest;
import io.github.artsobol.kurkod.feature.user.entity.User;
import io.github.artsobol.kurkod.feature.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AuthResponseFactory authResponseFactory;
  @Mock private UserService userService;
  @InjectMocks private RegistrationServiceImpl service;

  @Test
  void registerCreatesUserWithEncodedPasswordAndSession() {
    RegistrationRequest request = new RegistrationRequest("alice", "alice@example.com", "Password1!", "Password1!");
    SessionMetadata metadata = new SessionMetadata("127.0.0.1", "browser", "device");
    User user = User.create("alice", "alice@example.com", "{argon2}hash");
    AuthResponse expected = new AuthResponse("access", "refresh", null);
    when(passwordEncoder.encode("Password1!")).thenReturn("{argon2}hash");
    when(userService.createUser(any(CreateUserRequest.class))).thenReturn(user);
    when(authResponseFactory.create(any(CreateRefreshTokenRequest.class))).thenReturn(expected);

    AuthResponse actual = service.register(request, metadata);

    assertThat(actual).isSameAs(expected);
    ArgumentCaptor<CreateUserRequest> userRequest = ArgumentCaptor.forClass(CreateUserRequest.class);
    verify(userService).createUser(userRequest.capture());
    assertThat(userRequest.getValue().passwordHash()).isEqualTo("{argon2}hash");
    ArgumentCaptor<CreateRefreshTokenRequest> tokenRequest =
        ArgumentCaptor.forClass(CreateRefreshTokenRequest.class);
    verify(authResponseFactory).create(tokenRequest.capture());
    assertThat(tokenRequest.getValue().sessionId()).isNotNull();
    assertThat(tokenRequest.getValue().ipAddress()).isEqualTo("127.0.0.1");
  }
}
