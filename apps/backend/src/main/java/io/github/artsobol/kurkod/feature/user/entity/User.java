package io.github.artsobol.kurkod.feature.user.entity;

import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractEntity {

  @Getter
  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Getter
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Getter
  @Column(name = "email_verified_at")
  private Instant emailVerifiedAt;

  @Getter
  @Column(name = "password_hash", nullable = false, length = 255)
  private String passwordHash;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  public static User create(String username, String email, String passwordHash) {
    User entity = new User();
    entity.changeUsername(username);
    entity.changeEmail(email);
    entity.changePasswordHash(passwordHash);
    entity.changeRole(Role.USER);

    return entity;
  }

  public void changeUsername(String username) {
    String normalizedUsername =
        Objects.requireNonNull(username, "username must not be null").strip();
    if (normalizedUsername.isEmpty()) {
      throw new IllegalArgumentException("username must not be blank");
    }
    this.username = normalizedUsername;
  }

  public void changeEmail(String email) {
    String normalizedEmail =
        Objects.requireNonNull(email, "email must not be null").strip().toLowerCase();
    if (normalizedEmail.isEmpty()) {
      throw new IllegalArgumentException("email must not be blank");
    }
    this.email = normalizedEmail;
  }

  public void changePasswordHash(String passwordHash) {
    String normalizedPasswordHash =
        Objects.requireNonNull(passwordHash, "password hash must not be null").strip();
    if (normalizedPasswordHash.isEmpty()) {
      throw new IllegalArgumentException("password hash must not be blank");
    }
    this.passwordHash = normalizedPasswordHash;
  }

  public void changeRole(Role role) {
    Objects.requireNonNull(role, "Role must not be null");
    this.role = role;
  }

  public void verifyEmail() {
    if (this.emailVerifiedAt != null) {
      throw new IllegalArgumentException("User email already verified");
    }
    this.emailVerifiedAt = Instant.now();
  }
}
