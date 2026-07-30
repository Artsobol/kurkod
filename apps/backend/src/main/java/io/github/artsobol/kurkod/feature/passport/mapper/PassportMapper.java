package io.github.artsobol.kurkod.feature.passport.mapper;

import io.github.artsobol.kurkod.feature.passport.dto.response.PassportDTO;
import io.github.artsobol.kurkod.feature.passport.entity.Passport;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPatchRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPostRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PassportMapper {

    PassportDTO toDto(Passport passport);

    Passport toEntity(PassportPostRequest passportPostRequest);

    void updateFully(@MappingTarget Passport passport, PassportPutRequest passportPutRequest);

    void updatePartially(@MappingTarget Passport passport, PassportPatchRequest passportPatchRequest);
}
