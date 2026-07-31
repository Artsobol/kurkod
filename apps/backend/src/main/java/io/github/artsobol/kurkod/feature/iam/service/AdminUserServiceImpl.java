package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.iam.dto.request.ChangeRoleRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.iam.entity.RegistrationStatus;
import io.github.artsobol.kurkod.feature.iam.entity.Role;
import io.github.artsobol.kurkod.feature.iam.entity.SystemRole;
import io.github.artsobol.kurkod.feature.iam.entity.User;
import io.github.artsobol.kurkod.feature.iam.mapper.UserMapper;
import io.github.artsobol.kurkod.feature.iam.repository.RoleRepository;
import io.github.artsobol.kurkod.feature.iam.repository.UserRepository;
import io.github.artsobol.kurkod.infrastructure.util.VersionUtils;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponse changeUserRole(Long userId, ChangeRoleRequest request, Long expectedVersion) {
        User user = getUserById(userId);
        VersionUtils.checkVersion(user.getVersion(), expectedVersion);
        Role role = getRoleBySystemRole(request.role());

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse activateUser(Long userId, Long expectedVersion) {
        User user = getUserById(userId);
        VersionUtils.checkVersion(user.getVersion(), expectedVersion);
        user = changeStatus(user, RegistrationStatus.ACTIVE);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse deactivateUser(Long userId, Long expectedVersion) {
        User user = getUserById(userId);
        VersionUtils.checkVersion(user.getVersion(), expectedVersion);
        user = changeStatus(user, RegistrationStatus.INACTIVE);
        return userMapper.toResponse(user);
    }

    protected User changeStatus(User user, RegistrationStatus status) {
        user.setRegistrationStatus(status);
        return userRepository.save(user);
    }

    protected User getUserById(Long id) {
        return userRepository.findByIdAndIsActiveTrue(id)
                             .orElseThrow(() -> new NotFoundException("user.not.found.by.id", id));
    }

    protected Role getRoleBySystemRole(SystemRole role) {
        return roleRepository.findByUserSystemRole(role)
                .orElseThrow(() -> new NotFoundException("role.not.found", role.name()));
    }
}
