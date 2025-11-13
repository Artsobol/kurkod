package io.github.artsobol.kurkod.web.domain.refreshtoken.model.entity;

import io.github.artsobol.kurkod.web.domain.common.BaseEntity;
import io.github.artsobol.kurkod.web.domain.iam.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
