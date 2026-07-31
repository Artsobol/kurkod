package io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EggProductionMonthUpdateRequest {

    @Positive
    private Integer count;
}
