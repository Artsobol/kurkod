package io.github.artsobol.kurkod.feature.report.dto.response;

public record ChickensByWorkshopAndBreedResponse(
    Long workshopId, Integer workshopNumber, Long breedId, String breedName, Long chickensCount) {}
