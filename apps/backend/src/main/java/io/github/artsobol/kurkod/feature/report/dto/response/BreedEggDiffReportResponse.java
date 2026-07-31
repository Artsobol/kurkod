package io.github.artsobol.kurkod.feature.report.dto.response;

import java.math.BigDecimal;

public record BreedEggDiffReportResponse(
    Long breedId,
    String breedName,
    BigDecimal breedAvgEggs,
    BigDecimal farmAvgEggs,
    BigDecimal diffEggs) {}
