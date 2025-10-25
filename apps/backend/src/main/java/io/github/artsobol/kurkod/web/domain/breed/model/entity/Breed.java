package io.github.artsobol.kurkod.web.domain.breed.model.entity;

import io.github.artsobol.kurkod.web.domain.diet.model.entity.Diet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "breed")
@Getter
@Setter
@NoArgsConstructor
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @NotNull
    @Column(nullable = false, name = "deleted")
    private boolean deleted = false;

    @ManyToMany(mappedBy = "breeds")
    private Set<Diet> diets = new HashSet<>();

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Breed other)) return false;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return 31; }
}
