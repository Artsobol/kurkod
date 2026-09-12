package io.github.artsobol.kurkod.feature.breed.service;

import io.github.artsobol.kurkod.feature.breed.entity.Breed;

public interface BreedFinderService {

  Breed findByIdOrThrow(Long breedId);
}
