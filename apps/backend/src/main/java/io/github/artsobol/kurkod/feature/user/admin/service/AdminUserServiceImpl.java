package io.github.artsobol.kurkod.feature.user.admin.service;

import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.user.admin.dto.request.ChangeRoleRequest;
import io.github.artsobol.kurkod.feature.user.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.user.entity.User;
import io.github.artsobol.kurkod.feature.user.mapper.UserMapper;
import io.github.artsobol.kurkod.feature.user.repository.UserRepository;
import io.github.artsobol.kurkod.infrastructure.utils.VersionUtils;
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
    private final UserMapper userMapper;


    @Override
    public UserResponse changeUserRole(Long userId, ChangeRoleRequest request, Long expectedVersion) {
        User user = getUserById(userId);
        VersionUtils.checkVersion(user.getVersion(), expectedVersion);
        user.changeRole(request.role());

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse activateUser(Long userId, Long expectedVersion) {
        User user = getUserByIdIncludingInactive(userId);
        VersionUtils.checkVersion(user.getVersion(), expectedVersion);
        user.activate();
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse deactivateUser(Long userId, Long expectedVersion) {
        User user = getUserByIdIncludingInactive(userId);
        VersionUtils.checkVersion(user.getVersion(), expectedVersion);
        user.deactivate();
        return userMapper.toResponse(userRepository.save(user));
    }

    protected User getUserById(Long id) {
        return userRepository.findByIdAndIsActiveTrue(id)
                             .orElseThrow(() -> new NotFoundException("user.not.found.by.id", id));
    }

    protected User getUserByIdIncludingInactive(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user.not.found.by.id", id));
    }
}
