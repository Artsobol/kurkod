package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.dto.request.ChangeRoleRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserResponse;

public interface AdminUserService {

    UserResponse changeUserRole(Long userId, ChangeRoleRequest request, Long expectedVersion);

    UserResponse activateUser(Long userId, Long expectedVersion);

    UserResponse deactivateUser(Long userId, Long expectedVersion);
}
