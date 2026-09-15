package io.github.artsobol.kurkod.feature.diet.dto.request;

import io.github.artsobol.kurkod.feature.diet.entity.Season;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;


public record DietUpdateRequest(
  Set<Integer> addBreedsIds,
  Set<Integer> removeBreedsIds,
  @Pattern(regexp = "(?s).*\\S.*", message = "{diet.title.notBlank}") @Size(min = 2, max = 30) String title,
  @Pattern(regexp = "(?s).*\\S.*", message = "{diet.code.notBlank}")  @Size(min = 2, max = 10) String code,
  String description,
  Season season) {

  public DietUpdateRequest {
    addBreedsIds = addBreedsIds == null ? Set.of() : Set.copyOf(addBreedsIds);
    removeBreedsIds = removeBreedsIds == null ? Set.of() : Set.copyOf(removeBreedsIds);
  }
}
