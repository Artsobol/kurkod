package io.github.artsobol.kurkod.feature.user.admin.dto.request;

import io.github.artsobol.kurkod.feature.user.entity.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(@NotNull Role role) {}
