package io.github.artsobol.kurkod.feature.passport.mapper;

import io.github.artsobol.kurkod.feature.passport.dto.request.PassportCreateRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportUpdateRequest;
import io.github.artsobol.kurkod.feature.passport.dto.response.PassportResponse;
import io.github.artsobol.kurkod.feature.passport.entity.Passport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PassportMapper {

  PassportResponse toResponse(Passport passport);

  @Mapping(target = "worker", ignore = true)
  Passport toEntity(PassportCreateRequest passportCreateRequest);

  @Mapping(target = "worker", ignore = true)
  void updatePartially(
      @MappingTarget Passport passport, PassportUpdateRequest passportUpdateRequest);
}
