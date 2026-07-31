package io.github.artsobol.kurkod.feature.report.dto.response;


import java.time.LocalDate;

public record ChickenEggStatsResponse(
        Long chickenId,
        String chickenName,
        Long breedId,
        String breedName,
        Integer weight,
        LocalDate birthDate,
        Long eggsCount
) {}