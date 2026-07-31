package io.github.artsobol.kurkod.feature.eggproductionmonth.mapper;

import io.github.artsobol.kurkod.feature.chicken.mapper.ChickenMapper;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthResponse;
import io.github.artsobol.kurkod.feature.eggproductionmonth.entity.EggProductionMonth;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthUpdateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ChickenMapper.class)
public interface EggProductionMonthMapper {

    EggProductionMonthResponse toResponse(EggProductionMonth eggProductionMonth);

    EggProductionMonth toEntity(EggProductionMonthCreateRequest eggProductionMonthCreateRequest);
    void update(@MappingTarget EggProductionMonth eggProductionMonth, EggProductionMonthUpdateRequest eggProductionMonthUpdateRequest);


}
