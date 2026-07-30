package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.dto.request.ChangeRoleRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserDTO;

public interface AdminUserService {

    UserDTO changeUserRole(Long userId, ChangeRoleRequest request, Long expectedVersion);

    UserDTO activateUser(Long userId, Long expectedVersion);

    UserDTO deactivateUser(Long userId, Long expectedVersion);
}
