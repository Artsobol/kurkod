package io.github.artsobol.kurkod.feature.diet.dto.request;

import io.github.artsobol.kurkod.feature.diet.entity.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;


public record DietCreateRequest(
  Set<Integer> breedIds,
  @NotBlank @Size(min = 2, max = 30) String title,
  @NotBlank @Size(min = 2, max = 10) String code,
  String description,
  @NotNull Season season) {

  public DietCreateRequest {
    breedIds = Set.copyOf(breedIds);
  }
}
