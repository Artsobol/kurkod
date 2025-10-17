package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.UserMapper;
import io.github.artsobol.kurkod.error.impl.UserError;
import io.github.artsobol.kurkod.model.dto.user.UserDTO;
import io.github.artsobol.kurkod.model.entity.User;
import io.github.artsobol.kurkod.model.exception.DataExistException;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.user.UserPatchRequest;
import io.github.artsobol.kurkod.model.request.user.UserPostRequest;
import io.github.artsobol.kurkod.model.request.user.UserPutRequest;
import io.github.artsobol.kurkod.repository.UserRepository;
import io.github.artsobol.kurkod.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserDTO getById(@NotNull Integer userId) {
        return userMapper.toDto(getUserById(userId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public List<UserDTO> getAll() {
        return userRepository.findAllByIsActiveTrue().stream().map(userMapper::toDto).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserDTO getByUsername(@NotBlank String username) {
        User response = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND_BY_USERNAME.format(username)));
        return userMapper.toDto(response);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserDTO create(@NotNull UserPostRequest userPostRequest) {
        if (userRepository.existsByUsername(userPostRequest.getUsername())) {
            throw new DataExistException(UserError.WITH_USERNAME_ALREADY_EXISTS.format(userPostRequest.getUsername()));
        }
        if (userRepository.existsByEmail(userPostRequest.getEmail())) {
            throw new DataExistException(UserError.WITH_EMAIL_ALREADY_EXISTS.format(userPostRequest.getEmail()));
        }
        User user = userMapper.toEntity(userPostRequest);
        user.setPassword(passwordEncoder.encode(userPostRequest.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserDTO replace(@NotNull Integer userId, UserPutRequest userPutRequest) {
        User user = getUserById(userId);
        userMapper.updateFully(user, userPutRequest);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public UserDTO update(@NotNull Integer userId, UserPatchRequest userPatchRequest) {
        User user = getUserById(userId);
        userMapper.updatePartially(user, userPatchRequest);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void deleteById(@NotNull Integer userId) {
        User user = getUserById(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUserDetails(email, userRepository);
    }

    static UserDetails getUserDetails(String email, UserRepository userRepository) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(UserError.WITH_EMAIL_ALREADY_EXISTS.format(email)));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(
                        role -> new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList()));
    }

    protected User getUserById(Integer id) {
        return userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException(UserError.NOT_FOUND_BY_ID.format(id)));
    }
}
