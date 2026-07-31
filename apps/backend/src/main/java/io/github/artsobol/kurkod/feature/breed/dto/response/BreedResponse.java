package io.github.artsobol.kurkod.feature.breed.dto.response;

public record BreedResponse(
        Long id, String name, Integer eggsNumber, Integer weight, Long version
) {};
