package io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.dto;

import io.github.artsobol.kurkod.web.domain.chicken.model.dto.ChickenDTO;

public record EggProductionMonthDTO(
        Integer id, Integer month, Integer year, Integer count, ChickenDTO chicken, Long version
) {
}