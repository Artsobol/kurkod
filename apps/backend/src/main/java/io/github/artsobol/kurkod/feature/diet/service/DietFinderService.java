package io.github.artsobol.kurkod.feature.diet.service;

import io.github.artsobol.kurkod.feature.diet.entity.Diet;

public interface DietFinderService {

  Diet findByIdOrThrow(Long dietId);
}
