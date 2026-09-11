package io.github.artsobol.kurkod.feature.eggproductionmonth.mapper;

import io.github.artsobol.kurkod.feature.chicken.mapper.ChickenMapper;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthCreateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.request.EggProductionMonthUpdateRequest;
import io.github.artsobol.kurkod.feature.eggproductionmonth.dto.response.EggProductionMonthResponse;
import io.github.artsobol.kurkod.feature.eggproductionmonth.entity.EggProductionMonth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ChickenMapper.class)
public interface EggProductionMonthMapper {

  EggProductionMonthResponse toResponse(EggProductionMonth eggProductionMonth);

  @Mapping(target = "year", ignore = true)
  @Mapping(target = "month", ignore = true)
  @Mapping(target = "chicken", ignore = true)
  EggProductionMonth toEntity(EggProductionMonthCreateRequest eggProductionMonthCreateRequest);

  @Mapping(target = "year", ignore = true)
  @Mapping(target = "month", ignore = true)
  @Mapping(target = "chicken", ignore = true)
  void update(
      @MappingTarget EggProductionMonth eggProductionMonth,
      EggProductionMonthUpdateRequest eggProductionMonthUpdateRequest);
}
