package io.github.artsobol.kurkod.config.mapstruct;

import org.mapstruct.*;

@MapperConfig(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapStructConfig {}
