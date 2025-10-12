package io.github.artsobol.kurkod.mapper;

import io.github.artsobol.kurkod.model.dto.passport.PassportDTO;
import io.github.artsobol.kurkod.model.entity.Passport;
import io.github.artsobol.kurkod.model.request.passport.PassportPatchRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPostRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PassportMapper {

    PassportDTO toDto(Passport passport);

    Passport toEntity(PassportPostRequest passportPostRequest);

    void updateFully(@MappingTarget Passport passport, PassportPutRequest passportPutRequest);

    void updatePartially(@MappingTarget Passport passport, PassportPatchRequest passportPatchRequest);
}
