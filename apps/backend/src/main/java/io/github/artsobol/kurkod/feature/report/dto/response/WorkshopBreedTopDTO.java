package io.github.artsobol.kurkod.feature.report.dto.response;

public record WorkshopBreedTopDTO(
        Long workshopId,
        Long workshopNumber,
        Long breedId,
        String breedName,
        Long chickensCount
) {}