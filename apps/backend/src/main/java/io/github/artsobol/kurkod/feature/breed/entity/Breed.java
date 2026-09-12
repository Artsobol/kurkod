package io.github.artsobol.kurkod.feature.breed.entity;

import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import io.github.artsobol.kurkod.infrastructure.persistence.entity.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "breed")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Breed extends AbstractEntity {

  @Getter
  @NotBlank @Size(min = 2, max = 20) @Column(nullable = false, unique = true)
  private String name;

  @Getter
  @NotNull @PositiveOrZero @Column(nullable = false, name = "eggs_number")
  private Integer eggsNumber;

  @Getter
  @NotNull @PositiveOrZero @Column(nullable = false)
  private Integer weight;

  @ManyToMany(mappedBy = "breeds")
  private Set<Diet> diets = new HashSet<>();

  public static Breed create(String name, Integer eggsNumber, Integer weight) {
    Breed breed = new Breed();

    breed.changeName(name);
    breed.changeEggsNumber(eggsNumber);
    breed.changeWeight(weight);

    return breed;
  }

  public void updateDetails(String name, Integer eggsNumber, Integer weight) {
    if (name != null) {
      validateName(name);
    }
    if (eggsNumber != null) {
      validateEggsNumber(eggsNumber);
    }
    if (weight != null) {
      validateWeight(weight);
    }

    if (name != null) {
      changeName(name);
    }
    if (eggsNumber != null) {
      changeEggsNumber(eggsNumber);
    }
    if (weight != null) {
      changeWeight(weight);
    }
  }

  public void changeName(String name) {
    validateName(name);
    this.name = name;
  }

  public void changeEggsNumber(Integer eggsNumber) {
    validateEggsNumber(eggsNumber);
    this.eggsNumber = eggsNumber;
  }

  public void changeWeight(Integer weight) {
    validateWeight(weight);
    this.weight = weight;
  }

  public Set<Diet> getDiets() {
    return Set.copyOf(diets);
  }

  public void addDiet(Diet diet) {
    Objects.requireNonNull(diet, "Diet must not be null");

    if (diets.add(diet)) {
      diet.addBreed(this);
    }
  }

  public void removeDiet(Diet diet) {
    Objects.requireNonNull(diet, "Diet must not be null");

    if (diets.remove(diet)) {
      diet.removeBreed(this);
    }
  }

  private void validateName(String name) {
    if (name == null || name.isBlank() || name.length() < 2 || name.length() > 20) {
      throw new IllegalArgumentException(
          "Name must not be blank and must be between 2 and 20 characters");
    }
  }

  private static void validateEggsNumber(Integer eggsNumber) {
    if (eggsNumber == null || eggsNumber < 0) {
      throw new IllegalArgumentException("Eggs number cannot be null or negative");
    }
  }

  private static void validateWeight(Integer weight) {
    if (weight == null || weight < 0) {
      throw new IllegalArgumentException("Weight cannot be null or negative");
    }
  }
}
