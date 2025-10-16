package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.UserMapper;
import io.github.artsobol.kurkod.model.constants.ApiErrorMessage;
import io.github.artsobol.kurkod.model.dto.user.UserDTO;
import io.github.artsobol.kurkod.model.entity.User;
import io.github.artsobol.kurkod.model.exception.DataExistException;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.user.UserPatchRequest;
import io.github.artsobol.kurkod.model.request.user.UserPostRequest;
import io.github.artsobol.kurkod.model.request.user.UserPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.UserRepository;
import io.github.artsobol.kurkod.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public IamResponse<UserDTO> getUserById(@NotNull Integer userId) {
        User response = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));
        return IamResponse.createSuccessful(userMapper.toDto(response));
    }

    @Override
    public IamResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> response = userRepository.findAllByIsActiveTrue().stream().map(userMapper::toDto).toList();
        return IamResponse.createSuccessful(response);
    }

    @Override
    public IamResponse<UserDTO> getUserByUsername(@NotBlank String username) {
        User response = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_USERNAME.getMessage(username)));
        return IamResponse.createSuccessful(userMapper.toDto(response));
    }

    @Override
    public IamResponse<UserDTO> createUser(@NotNull UserPostRequest userPostRequest) {
        if (userRepository.existsByUsername(userPostRequest.getUsername())) {
            throw new DataExistException(ApiErrorMessage.USER_WITH_USERNAME_ALREADY_EXISTS.getMessage(userPostRequest.getUsername()));
        }
        if (userRepository.existsByEmail(userPostRequest.getEmail())) {
            throw new DataExistException(ApiErrorMessage.USER_WITH_EMAIL_ALREADY_EXISTS.getMessage(userPostRequest.getEmail()));
        }
        User user = userMapper.toEntity(userPostRequest);
        user.setPassword(passwordEncoder.encode(userPostRequest.getPassword()));
        user = userRepository.save(user);
        return IamResponse.createSuccessful(userMapper.toDto(user));
    }

    @Override
    public IamResponse<UserDTO> updateFullyUser(@NotNull Integer userId, UserPutRequest userPutRequest) {
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));
        userMapper.updateFully(user, userPutRequest);
        user = userRepository.save(user);
        return IamResponse.createSuccessful(userMapper.toDto(user));
    }

    @Override
    public IamResponse<UserDTO> updatePartiallyUser(@NotNull Integer userId, UserPatchRequest userPatchRequest) {
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));
        userMapper.updatePartially(user, userPatchRequest);
        user = userRepository.save(user);
        return IamResponse.createSuccessful(userMapper.toDto(user));
    }

    @Override
    public void deleteUser(@NotNull Integer userId) {
        User user = userRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_ID.getMessage(userId)));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUserDetails(email, userRepository);
    }

    static UserDetails getUserDetails(String email, UserRepository userRepository) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.USER_NOT_FOUND_BY_EMAIL.getMessage(email)));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getName())
        ).collect(Collectors.toList()));
    }
}
