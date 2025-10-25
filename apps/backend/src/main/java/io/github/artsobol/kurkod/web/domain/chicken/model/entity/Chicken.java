package io.github.artsobol.kurkod.web.domain.chicken.model.entity;

import io.github.artsobol.kurkod.web.domain.breed.model.entity.Breed;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chicken")
public class Chicken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer weight;

    @NotNull
    @Column(nullable = false, name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false)
    private Breed breed;

    @NotNull
    @Column(nullable = false, name = "deleted")
    private boolean deleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
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
