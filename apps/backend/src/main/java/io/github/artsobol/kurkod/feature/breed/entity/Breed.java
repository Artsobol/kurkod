package io.github.artsobol.kurkod.feature.breed.entity;

import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "breed")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Breed extends AbstractEntity {

    @NotBlank
    @Size(min = 2, max = 20, message = "Name should be between 2 and 20 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(nullable = false, name = "eggs_number")
    private Integer eggsNumber;

    @NotNull
    @Column(nullable = false)
    private Integer weight;

    @ManyToMany(mappedBy = "breeds")
    private Set<Diet> diets = new HashSet<>();
}
