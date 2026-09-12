package io.github.artsobol.kurkod.feature.breed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

public record BreedCreateRequest(
    @NotBlank @Size(min = 2, max = 20) String name,
    @NotNull @Positive Integer eggsNumber,
    @NotNull @Positive Integer weight) {}
