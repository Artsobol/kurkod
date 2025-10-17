package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.UserMapper;
import io.github.artsobol.kurkod.error.impl.AuthError;
import io.github.artsobol.kurkod.model.entity.Role;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.user.LoginRequest;
import io.github.artsobol.kurkod.model.dto.user.UserProfileDTO;
import io.github.artsobol.kurkod.model.entity.RefreshToken;
import io.github.artsobol.kurkod.model.entity.User;
import io.github.artsobol.kurkod.model.exception.InvalidDataException;
import io.github.artsobol.kurkod.model.request.user.RegistrationUserRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.RoleRepository;
import io.github.artsobol.kurkod.repository.UserRepository;
import io.github.artsobol.kurkod.security.JwtTokenProvider;
import io.github.artsobol.kurkod.security.validation.AccessValidator;
import io.github.artsobol.kurkod.service.AuthService;
import io.github.artsobol.kurkod.service.RefreshTokenService;
import io.github.artsobol.kurkod.service.model.ServiceUserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessValidator accessValidator;

    @Override
    public IamResponse<UserProfileDTO> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidDataException(AuthError.INVALID_USER_OR_PASSWORD.format());
        }

        User user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new InvalidDataException(AuthError.INVALID_USER_OR_PASSWORD.format()));

        RefreshToken refreshToken = refreshTokenService.generateOrUpdateRefreshToken(user);
        String token = jwtTokenProvider.generateToken(user);
        UserProfileDTO userProfileDTO = userMapper.toUserProfileDto(user, token, refreshToken.getToken());
        userProfileDTO.setToken(token);

        return IamResponse.createSuccessful(userProfileDTO);
    }

    @Override
    public IamResponse<UserProfileDTO> refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenService.validateAndRefreshToken(refreshTokenValue);
        User user = refreshToken.getUser();
        String accessToken = jwtTokenProvider.generateToken(user);
        return IamResponse.createSuccessfulWithNewToken(userMapper.toUserProfileDto(user,
                accessToken,
                refreshToken.getToken()));
    }

    @Override
    @Transactional
    public IamResponse<UserProfileDTO> registerUser(@NotNull RegistrationUserRequest request) {
        accessValidator.validateNewUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getConfirmPassword()
        );

        Role userRole = roleRepository.findByName(ServiceUserRole.USER.getRole())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        User newUser = userMapper.fromDto(request);

        String enc = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(enc);
        newUser.setRoles(Set.of(userRole));
        newUser = userRepository.save(newUser);

        RefreshToken refreshToken = refreshTokenService.generateOrUpdateRefreshToken(newUser);
        String token = jwtTokenProvider.generateToken(newUser);

        return IamResponse.createSuccessfulWithNewToken(
                userMapper.toUserProfileDto(newUser, token, refreshToken.getToken())
        );
    }

}
