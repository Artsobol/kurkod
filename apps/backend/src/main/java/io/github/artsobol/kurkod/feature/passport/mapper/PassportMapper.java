package io.github.artsobol.kurkod.feature.passport.mapper;

import io.github.artsobol.kurkod.feature.passport.dto.response.PassportResponse;
import io.github.artsobol.kurkod.feature.passport.entity.Passport;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportUpdateRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PassportMapper {

    PassportResponse toResponse(Passport passport);

    Passport toEntity(PassportCreateRequest passportCreateRequest);
    void updatePartially(@MappingTarget Passport passport, PassportUpdateRequest passportUpdateRequest);
}
