package io.github.artsobol.kurkod.feature.report.dto.response;

import java.math.BigDecimal;

public record BreedEggDiffReportDTO(
        Long breedId,
        String breedName,
        BigDecimal breedAvgEggs,
        BigDecimal farmAvgEggs,
        BigDecimal diffEggs
) {}