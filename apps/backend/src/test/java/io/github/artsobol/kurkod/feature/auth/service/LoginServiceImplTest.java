package io.github.artsobol.kurkod.feature.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.exception.security.AuthenticationException;
import io.github.artsobol.kurkod.feature.auth.dto.request.LoginRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.kurkod.feature.user.entity.User;
import io.github.artsobol.kurkod.feature.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

  @Mock private UserService userService;
  @Mock private AuthResponseFactory authResponseFactory;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private LoginServiceImpl service;

  private final SessionMetadata metadata = new SessionMetadata("127.0.0.1", "browser", "device");

  @Test
  void loginRejectsWrongPassword() {
    User user = User.create("alice", "alice@example.com", "hash");
    when(userService.findActiveByEmail("alice@example.com")).thenReturn(user);
    when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);

    assertThatThrownBy(
            () -> service.login(new LoginRequest("alice@example.com", "wrong"), metadata))
        .isInstanceOf(AuthenticationException.class);
  }

  @Test
  void loginDoesNotRevealUnknownEmail() {
    when(userService.findActiveByEmail("missing@example.com"))
        .thenThrow(new NotFoundException("user.not.found.by.email", "missing@example.com"));

    assertThatThrownBy(
            () -> service.login(new LoginRequest("missing@example.com", "wrong"), metadata))
        .isInstanceOf(AuthenticationException.class);
  }
}
