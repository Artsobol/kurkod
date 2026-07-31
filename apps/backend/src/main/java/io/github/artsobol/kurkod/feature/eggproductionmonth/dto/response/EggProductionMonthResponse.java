package io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response;

import io.github.artsobol.kurkod.feature.chicken.dto.response.ChickenResponse;

public record EggProductionMonthResponse(
    Integer id,
    Integer month,
    Integer year,
    Integer count,
    ChickenResponse chicken,
    Long version) {}
