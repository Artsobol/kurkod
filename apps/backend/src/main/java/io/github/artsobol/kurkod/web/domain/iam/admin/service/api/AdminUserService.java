package io.github.artsobol.kurkod.web.domain.iam.admin.service.api;

import io.github.artsobol.kurkod.web.domain.iam.admin.model.dto.ChangeRoleRequest;
import io.github.artsobol.kurkod.web.domain.iam.user.model.dto.UserDTO;

public interface AdminUserService {

    UserDTO changeUserRole(Integer userId, ChangeRoleRequest request, Long expectedVersion);

    UserDTO activateUser(Integer userId, Long expectedVersion);

    UserDTO deactivateUser(Integer userId, Long expectedVersion);
}
