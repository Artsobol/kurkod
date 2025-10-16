package io.github.artsobol.kurkod.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceUserRole {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN");

    private final String role;

    public static ServiceUserRole fromString(String role) {
        return ServiceUserRole.valueOf(role.toUpperCase());
    }
}
