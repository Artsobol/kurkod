package io.github.artsobol.kurkod.feature.iam.dto.response;

import io.github.artsobol.kurkod.feature.iam.dto.response.RoleDTO;
import io.github.artsobol.kurkod.feature.iam.entity.UserRole;
import io.github.artsobol.kurkod.feature.iam.entity.RegistrationStatus;

import java.time.OffsetDateTime;
import java.util.List;

public record UserDTO(
        Long id,
        String username,
        String email,
        UserRole role,
        RegistrationStatus registrationStatus,
        List<RoleDTO> roles,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {
};
