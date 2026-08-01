package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.feature.user.entity.User;
import io.github.artsobol.kurkod.infrastructure.security.jwt.JwtSubject;
import io.github.artsobol.kurkod.infrastructure.security.jwt.JwtTokenProvider;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public String createAccessToken(User user, UUID sessionId) {
    Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.getRole().name()));
    return jwtTokenProvider.generateToken(
        new JwtSubject(user.getId(), authorities, user.getUsername(), sessionId));
  }
}
