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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diet extends AbstractEntity {

  @Getter
  @NotBlank
  @Size(min = 2, max = 30)
  @Column(nullable = false, unique = true)
  private String title;

  @Getter
  @NotBlank
  @Size(min = 2, max = 10)
  @Column(nullable = false, unique = true)
  private String code;

  @Getter
  private String description;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 6)
  private Season season;

  @ManyToMany
  @JoinTable(
    name = "breed_diet",
    joinColumns = @JoinColumn(name = "diet_id"),
    inverseJoinColumns = @JoinColumn(name = "breed_id"))
  private Set<Breed> breeds = new HashSet<>();

  public static Diet create(String title, String description, String code, Season season) {
    Diet diet = new Diet();
    diet.changeTitle(title);
    diet.changeDescription(description);
    diet.changeCode(code);
    diet.changeSeason(season);

    return diet;
  }

  public void updateDetails(String title, String description, String code, Season season) {
    if (title != null) {
      validateTitle(title);
    }
    if (code != null) {
      validateCode(code);
    }
    if (season != null) {
      validateSeason(season);
    }

    if (title != null) {
      this.title = title;
    }
    if (description != null) {
      this.description = description;
    }
    if (code != null) {
      this.code = code;
    }
    if (season != null) {
      this.season = season;
    }
  }

  public void changeTitle(String title) {
    validateTitle(title);
    this.title = title;
  }

  public void changeDescription(String description) {
    this.description = description;
  }

  public void changeCode(String code) {
    validateCode(code);
    this.code = code;
  }

  public void changeSeason(Season season) {
    validateSeason(season);
    this.season = season;
  }

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

  public void clearBreeds() {
    for (Breed breed : Set.copyOf(breeds)) {
      breed.removeDiet(this);
    }
    breeds.clear();
  }

  public Set<Breed> getBreeds() {
    return Set.copyOf(breeds);
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


  private void validateTitle(String title) {
    if (title == null || title.isBlank() || title.length() < 2 || title.length() > 30) {
      throw new IllegalArgumentException(
        "Title must not be blank and must be between 2 and 30 characters");
    }
  }

  private void validateCode(String code) {
    if (code == null || code.isBlank() || code.length() < 2 || code.length() > 10) {
      throw new IllegalArgumentException(
        "Code must not be blank and must be between 2 and 10 characters");
    }
  }

  private void validateSeason(Season season) {
    if (season == null) {
      throw new IllegalArgumentException(
        "Season not be null");
    }
  }
}
