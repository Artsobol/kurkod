package io.github.artsobol.kurkod.feature.diet.dto.response;

import io.github.artsobol.kurkod.infrastructure.persistence.entity.Season;

public record DietDTO(
        Integer id, String title, String code, String description, Season season, Long version
) {
};
