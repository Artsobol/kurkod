package io.github.artsobol.kurkod.infrastructure.security.jwt;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;

public record JwtSubject(
    Long userId,
    Collection<? extends GrantedAuthority> authorities,
    String username,
    UUID sessionId) {
  public JwtSubject {
    authorities = List.copyOf(authorities);
  }
}
