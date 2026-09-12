package io.github.artsobol.kurkod.feature.diet.entity;

import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "diet")
public class Diet extends AbstractEntity {

  @NotBlank @Size(min = 2, max = 30, message = "Title should be between 2 and 30 characters") @Column(nullable = false, unique = true)
  private String title;

  @NotBlank @Size(min = 2, max = 10, message = "Code should be between 2 and 10 characters") @Column(nullable = false, unique = true)
  private String code;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "season", nullable = false, length = 6)
  private Season season;

  @ManyToMany
  @JoinTable(
      name = "breed_diet",
      joinColumns = @JoinColumn(name = "diet_id"),
      inverseJoinColumns = @JoinColumn(name = "breed_id"))
  private Set<Breed> breeds = new HashSet<>();

  public void addBreed(Breed breed) {
    Objects.requireNonNull(breed, "Breed must not be null");

    if (breeds.add(breed)) {
      breed.addDiet(this);
    }
  }

  public void removeBreed(Breed breed) {
    Objects.requireNonNull(breed, "Breed must not be null");

    if (breeds.remove(breed)) {
      breed.removeDiet(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Diet other)) {
      return false;
    }
    return id != null && id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
