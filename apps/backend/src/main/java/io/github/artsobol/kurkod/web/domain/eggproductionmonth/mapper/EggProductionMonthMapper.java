package io.github.artsobol.kurkod.web.domain.eggproductionmonth.mapper;

import io.github.artsobol.kurkod.web.domain.chicken.mapper.ChickenMapper;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.dto.EggProductionMonthDTO;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.entity.EggProductionMonth;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPatchRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPostRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ChickenMapper.class)
public interface EggProductionMonthMapper {

    EggProductionMonthDTO toDto(EggProductionMonth eggProductionMonth);

    EggProductionMonth toEntity(EggProductionMonthPostRequest eggProductionMonthPostRequest);

    void replace(@MappingTarget EggProductionMonth eggProductionMonth, EggProductionMonthPutRequest eggProductionMonthPutRequest);

    void update(@MappingTarget EggProductionMonth eggProductionMonth, EggProductionMonthPatchRequest eggProductionMonthPatchRequest);


}
