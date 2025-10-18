package io.github.artsobol.kurkod.web.domain.staff.model.entity;

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
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String position;

    @NotNull
    @Column(nullable = false, name = "is_active")
    private boolean isActive = true;

    @NotNull
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
