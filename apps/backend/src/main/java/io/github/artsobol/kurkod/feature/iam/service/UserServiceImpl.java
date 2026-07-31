package io.github.artsobol.kurkod.feature.iam.service;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserCreateRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserUpdateRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.iam.entity.User;
import io.github.artsobol.kurkod.feature.iam.mapper.UserMapper;
import io.github.artsobol.kurkod.feature.iam.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    static UserDetails getUserDetails(String email, UserRepository userRepository) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user.not.found.by.email", email));

        user.setLastLogin(Instant.now());
        userRepository.save(user);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(
                        role -> new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserResponse getById(@NotNull Long userId) {
        return userMapper.toResponse(getUserById(userId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public List<UserResponse> getAll() {
        return userRepository.findAllByIsActiveTrue().stream().map(userMapper::toResponse).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserResponse getByUsername(@NotBlank String username) {
        User response = getUserByUsername(username);
        return userMapper.toResponse(response);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserResponse create(@NotNull UserCreateRequest request) {
        ensureNotExistsByUsername(request.getUsername());
        ensureNotExistsByEmail(request.getEmail());
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserResponse update(@NotNull Long userId, UserUpdateRequest request,Long version) {
        User user = getUserById(userId);
        checkVersion(user.getVersion(), version);
        userMapper.updatePartially(user, request);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void deleteById(@NotNull Long userId, Long version) {
        User user = getUserById(userId);
        checkVersion(user.getVersion(), version);
        user.deactivate();
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUserDetails(email, userRepository);
    }

    protected User getUserByUsername(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new NotFoundException("user.not.found.by.username", username));
    }

    protected User getUserById(Long id) {
        return userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("user.not.found.by.id", id));
    }

    protected void ensureNotExistsByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DataExistException("user.username.already.exists", username);
        }
    }

    protected void ensureNotExistsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DataExistException("user.email.already.exists", email);
        }
    }
}
