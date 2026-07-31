package io.github.artsobol.kurkod.feature.breed.service;

import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import io.github.artsobol.kurkod.feature.breed.repository.BreedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BreedLookupService {

  private final BreedRepository breedRepository;

  public Breed getBreedByIdOrThrow(Long breedId) {
    return breedRepository
        .findBreedByIdAndIsActiveTrue(breedId)
        .orElseThrow(() -> new NotFoundException("breed.not.found", breedId));
  }
}
