package io.github.artsobol.kurkod.feature.chicken.entity;

import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chicken")
public class Chicken extends AbstractEntity {

    @NotBlank
    @Column(length = 30, nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer weight;

    @NotNull
    @Column(nullable = false, name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breed_id", nullable = false)
    private Breed breed;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cage_id", nullable = false)
    private Cage cage;
}
