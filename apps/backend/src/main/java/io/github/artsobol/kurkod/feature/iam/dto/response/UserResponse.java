package io.github.artsobol.kurkod.feature.iam.dto.response;

import io.github.artsobol.kurkod.feature.iam.entity.RegistrationStatus;
import io.github.artsobol.kurkod.feature.iam.entity.UserRole;
import java.time.Instant;
import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String email,
        UserRole role,
        RegistrationStatus registrationStatus,
        List<RoleResponse> roles,
        Instant createdAt,
        Instant updatedAt,
        Long version
) {
}
