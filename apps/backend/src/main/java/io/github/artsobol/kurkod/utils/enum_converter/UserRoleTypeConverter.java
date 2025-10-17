package io.github.artsobol.kurkod.utils.enum_converter;

import io.github.artsobol.kurkod.service.model.ServiceUserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserRoleTypeConverter implements AttributeConverter<ServiceUserRole, String> {
    @Override
    public String convertToDatabaseColumn(ServiceUserRole userRole) {
        return userRole.name();
    }

    @Override
    public ServiceUserRole convertToEntityAttribute(String s) {
        return ServiceUserRole.fromString(s);
    }
}
