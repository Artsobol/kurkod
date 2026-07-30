package io.github.artsobol.kurkod.infrastructure.logging;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogHelper {

    public static String getEntityName(Object entity) {
        if (entity == null) {
            return "UnknownEntity";
        }
        return entity.getClass().getSimpleName();
    }

    public String getEntityName(Class<?> type) {
        return type == null ? "UnknownEntity" : type.getSimpleName();
    }
}
