package io.github.artsobol.kurkod.feature.iam.entity;

import io.github.artsobol.kurkod.infrastructure.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_role")
public class UserRole extends BaseEntity {

    private String title;
}
