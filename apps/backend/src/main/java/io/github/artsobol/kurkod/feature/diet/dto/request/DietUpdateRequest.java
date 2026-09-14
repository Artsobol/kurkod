package io.github.artsobol.kurkod.feature.diet.dto.request;

import io.github.artsobol.kurkod.feature.diet.entity.Season;
import jakarta.validation.constraints.Size;

import java.util.Set;


public record DietUpdateRequest(
  Set<Integer> addBreedsIds,
  Set<Integer> removeBreedsIds,
  @Size(min = 2, max = 30) String title,
  @Size(min = 2, max = 10) String code,
  String description,
  Season season) {

  public DietUpdateRequest {
    addBreedsIds = Set.copyOf(addBreedsIds);
    removeBreedsIds = Set.copyOf(removeBreedsIds);
  }
}
