package io.github.artsobol.kurkod.infrastructure.persistence.converter;

import io.github.artsobol.kurkod.feature.iam.entity.SystemRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserRoleTypeConverter implements AttributeConverter<SystemRole, String> {
    @Override
    public String convertToDatabaseColumn(SystemRole userRole) {
        return userRole.name();
    }

    @Override
    public SystemRole convertToEntityAttribute(String s) {
        return SystemRole.fromString(s);
    }
}
