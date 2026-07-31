package io.github.artsobol.kurkod.feature.diet.dto.response;

import io.github.artsobol.kurkod.feature.diet.entity.Season;

public record DietResponse(
    Integer id, String title, String code, String description, Season season, Long version) {}
