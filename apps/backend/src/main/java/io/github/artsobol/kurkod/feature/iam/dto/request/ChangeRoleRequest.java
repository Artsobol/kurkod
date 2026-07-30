package io.github.artsobol.kurkod.feature.iam.dto.request;

import io.github.artsobol.kurkod.feature.iam.entity.SystemRole;

public record ChangeRoleRequest(SystemRole role) {
}
