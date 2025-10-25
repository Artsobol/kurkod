package io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.dto;

import io.github.artsobol.kurkod.web.domain.chicken.model.dto.ChickenDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EggProductionMonthDTO {

    private Integer id;

    private Integer month;

    private Integer year;

    private Integer eggsCount;

    private Integer chickenId;
}
