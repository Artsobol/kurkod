package io.github.artsobol.kurkod.feature.eggproductionmonth.mapper;

import io.github.artsobol.kurkod.feature.chicken.mapper.ChickenMapper;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthDTO;
import io.github.artsobol.kurkod.feature.eggproductionmonth.entity.EggProductionMonth;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPatchRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPostRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ChickenMapper.class)
public interface EggProductionMonthMapper {

    EggProductionMonthDTO toDto(EggProductionMonth eggProductionMonth);

    EggProductionMonth toEntity(EggProductionMonthPostRequest eggProductionMonthPostRequest);

    void replace(@MappingTarget EggProductionMonth eggProductionMonth, EggProductionMonthPutRequest eggProductionMonthPutRequest);

    void update(@MappingTarget EggProductionMonth eggProductionMonth, EggProductionMonthPatchRequest eggProductionMonthPatchRequest);


}
