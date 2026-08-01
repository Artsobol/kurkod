package io.github.artsobol.kurkod.feature.user.service;

import static io.github.artsobol.kurkod.infrastructure.utils.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.user.dto.request.CreateUserRequest;
import io.github.artsobol.kurkod.feature.user.dto.request.UserCreateRequest;
import io.github.artsobol.kurkod.feature.user.dto.request.UserUpdateRequest;
import io.github.artsobol.kurkod.feature.user.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.user.entity.User;
import io.github.artsobol.kurkod.feature.user.mapper.UserMapper;
import io.github.artsobol.kurkod.feature.user.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public User createUser(CreateUserRequest request) {
    ensureNotExistsByUsername(request.username());
    ensureNotExistsByEmail(request.email());

    User user = User.create(request.username(), request.email(), request.passwordHash());
    userRepository.save(user);
    log.info("User created userId={} username={}", user.getId(), user.getUsername());
    return user;
  }

  @Override
  public User findActiveByEmail(String email) {
    return userRepository.findByEmailAndIsActiveTrue(email)
        .orElseThrow(() -> new NotFoundException("user.not.found.by.email", email));
  }

  @Override
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public UserResponse getById(@NotNull Long userId) {
    return userMapper.toResponse(getActiveUserById(userId));
  }

  @Override
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public List<UserResponse> getAll() {
    return userRepository.findAllByIsActiveTrue().stream().map(userMapper::toResponse).toList();
  }

  @Override
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public UserResponse getByUsername(@NotBlank String username) {
    User user = userRepository.findByUsernameAndIsActiveTrue(username)
        .orElseThrow(() -> new NotFoundException("user.not.found.by.username", username));
    return userMapper.toResponse(user);
  }

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public UserResponse create(@NotNull UserCreateRequest request) {
    User user = createUser(new CreateUserRequest(
        request.getUsername(),
        request.getEmail(),
        passwordEncoder.encode(request.getPassword())));
    return userMapper.toResponse(user);
  }

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public UserResponse update(@NotNull Long userId, UserUpdateRequest request, Long version) {
    User user = getActiveUserById(userId);
    checkVersion(user.getVersion(), version);

    if (StringUtils.hasText(request.getUsername()) && !request.getUsername().equals(user.getUsername())) {
      ensureNotExistsByUsername(request.getUsername());
      user.changeUsername(request.getUsername());
    }
    if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
      ensureNotExistsByEmail(request.getEmail());
      user.changeEmail(request.getEmail());
    }
    if (StringUtils.hasText(request.getPassword())) {
      user.changePasswordHash(passwordEncoder.encode(request.getPassword()));
    }

    return userMapper.toResponse(userRepository.save(user));
  }

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public void deleteById(@NotNull Long userId, Long version) {
    User user = getActiveUserById(userId);
    checkVersion(user.getVersion(), version);
    user.deactivate();
    userRepository.save(user);
  }

  private User getActiveUserById(Long id) {
    return userRepository.findByIdAndIsActiveTrue(id)
        .orElseThrow(() -> new NotFoundException("user.not.found.by.id", id));
  }

  private void ensureNotExistsByUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new DataExistException("user.username.already.exists", username);
    }
  }

  private void ensureNotExistsByEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DataExistException("user.email.already.exists", email);
    }
  }
}
