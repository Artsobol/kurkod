package io.github.artsobol.kurkod.feature.diet.mapper;

import io.github.artsobol.kurkod.config.mapstruct.MapStructConfig;
import io.github.artsobol.kurkod.feature.diet.dto.response.DietResponse;
import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface DietMapper {

  DietResponse toResponse(Diet diet);
}
