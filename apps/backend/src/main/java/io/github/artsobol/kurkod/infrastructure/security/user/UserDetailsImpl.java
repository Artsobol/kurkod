package io.github.artsobol.kurkod.infrastructure.security.user;

import io.github.artsobol.kurkod.feature.user.entity.Role;
import io.github.artsobol.kurkod.feature.user.entity.User;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

  private final String username;
  private final String passwordHash;
  private final Role role;
  private final boolean active;

  public UserDetailsImpl(User user) {
    this.username = user.getUsername();
    this.passwordHash = user.getPasswordHash();
    this.role = user.getRole();
    this.active = user.isActive();
  }

  @Override
  @NullMarked
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public @Nullable String getPassword() {
    return passwordHash;
  }

  @Override
  @NullMarked
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return active;
  }
}
