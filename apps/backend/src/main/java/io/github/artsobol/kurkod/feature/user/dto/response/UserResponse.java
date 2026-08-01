package io.github.artsobol.kurkod.feature.user.dto.response;

import io.github.artsobol.kurkod.feature.user.entity.Role;
import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        Long version
) {
}
