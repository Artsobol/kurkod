package io.github.artsobol.kurkod.feature.breed.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BreedUpdateRequest(
    @Size(min = 2, max = 20) @Pattern(
            regexp = "(?s).*\\P{javaWhitespace}.*",
            message = "{jakarta.validation.constraints.NotBlank.message}")
        String name,
    @Positive Integer eggsNumber,
    @Positive Integer weight) {}
