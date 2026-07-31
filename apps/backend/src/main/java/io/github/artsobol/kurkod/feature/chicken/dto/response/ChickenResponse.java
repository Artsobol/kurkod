package io.github.artsobol.kurkod.feature.chicken.dto.response;

import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;

import java.time.LocalDate;

public record ChickenResponse(
    Long id,
    String name,
    Integer weight,
    LocalDate birthDate,
    BreedResponse breed,
    CageResponse cage,
    Long version) {}
