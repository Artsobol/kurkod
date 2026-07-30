package io.github.artsobol.kurkod.feature.staff.entity;

import io.github.artsobol.kurkod.infrastructure.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@Getter
@Setter
public class Staff extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String position;
}
