package io.github.artsobol.kurkod.feature.report.dto.response;

import java.math.BigDecimal;

public record BreedWorkshopMonthlyReportDTO(
        Long workshopId,
        Integer workshopNumber,
        Long breedId,
        String breedName,
        Long chickensCount,
        Long eggsTotal,
        BigDecimal avgEggsPerChicken
) {}
