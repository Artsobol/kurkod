package io.github.artsobol.kurkod.web.domain.cage.mapper;

import io.github.artsobol.kurkod.web.domain.cage.model.dto.CageDTO;
import io.github.artsobol.kurkod.web.domain.cage.model.entity.Cage;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePatchRequest;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePostRequest;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CageMapper {

    @Mapping(target = "rowNumber", ignore = true)
    CageDTO toDto(Cage cage);

    Cage toEntity(CagePostRequest cagePostRequest);

    void replace(@MappingTarget Cage cage, CagePutRequest cagePutRequest);

    void update(@MappingTarget Cage cage, CagePatchRequest cagePatchRequest);
}
