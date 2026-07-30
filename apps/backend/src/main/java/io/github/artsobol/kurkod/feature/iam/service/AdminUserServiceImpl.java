package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.util.VersionUtils;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.iam.dto.request.ChangeRoleRequest;
import io.github.artsobol.kurkod.feature.iam.service.AdminUserService;
import io.github.artsobol.kurkod.feature.iam.error.RoleError;
import io.github.artsobol.kurkod.feature.iam.entity.Role;
import io.github.artsobol.kurkod.feature.iam.repository.RoleRepository;
import io.github.artsobol.kurkod.feature.iam.error.UserError;
import io.github.artsobol.kurkod.feature.iam.mapper.UserMapper;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserDTO;
import io.github.artsobol.kurkod.feature.iam.entity.User;
import io.github.artsobol.kurkod.feature.iam.entity.RegistrationStatus;
import io.github.artsobol.kurkod.feature.iam.entity.SystemRole;
import io.github.artsobol.kurkod.feature.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@Transactional
@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    public UserDTO changeUserRole(Long userId, ChangeRoleRequest request, Long expectedVersion) {
        VersionUtils.checkVersion(expectedVersion, getUserById(userId).getVersion());
        User user = getUserById(userId);
        Role role = getRoleBySystemRole(request.role());

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(User.class), userId);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO activateUser(Long userId, Long expectedVersion) {
        VersionUtils.checkVersion(expectedVersion, getUserById(userId).getVersion());
        User user = changeStatus(getUserById(userId), RegistrationStatus.ACTIVE);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(User.class), userId);
        return userMapper.toDto(user);
    }

    @Override
    public UserDTO deactivateUser(Long userId, Long expectedVersion) {
        VersionUtils.checkVersion(expectedVersion, getUserById(userId).getVersion());
        User user = changeStatus(getUserById(userId), RegistrationStatus.INACTIVE);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(User.class), userId);
        return userMapper.toDto(user);
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
