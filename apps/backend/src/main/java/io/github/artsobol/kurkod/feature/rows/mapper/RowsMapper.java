package io.github.artsobol.kurkod.feature.rows.mapper;

import io.github.artsobol.kurkod.feature.rows.dto.response.RowsDTO;
import io.github.artsobol.kurkod.feature.rows.entity.Rows;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsUpdateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RowsMapper {

    @Mapping(target = "workshopId", source = "workshop.id")
    RowsDTO toDto(Rows rows);

    @Mapping(target = "workshop", ignore = true)
    Rows toEntity(RowsCreateRequest rowsCreateRequest);

    @Mapping(target = "workshop", ignore = true)
    void update(@MappingTarget Rows rows, RowsUpdateRequest rowsUpdateRequest);
}
