package io.github.artsobol.kurkod.feature.report.dto.response;

public record WorkshopBreedTopResponse(
    Long workshopId, Long workshopNumber, Long breedId, String breedName, Long chickensCount) {}
