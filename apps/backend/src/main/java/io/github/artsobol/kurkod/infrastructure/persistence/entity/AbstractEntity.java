package io.github.artsobol.kurkod.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  protected Instant createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  protected Instant updatedAt;

  @Column(nullable = false, name = "is_active")
  protected boolean isActive = true;

  @Version protected Long version;

  public void activate() {
    if (this.isActive) {
      throw new IllegalStateException("Entity is already active");
    }
    this.isActive = true;
  }

  public void deactivate() {
    if (!this.isActive) {
      throw new IllegalStateException("Entity is not active");
    }
    this.isActive = false;
  }
}
