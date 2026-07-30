package io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response;

import io.github.artsobol.kurkod.feature.chicken.dto.response.ChickenDTO;

public record EggProductionMonthDTO(
        Integer id, Integer month, Integer year, Integer count, ChickenDTO chicken, Long version
) {
}