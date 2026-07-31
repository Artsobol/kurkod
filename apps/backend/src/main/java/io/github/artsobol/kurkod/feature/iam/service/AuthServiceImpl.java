package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.mapper.UserMapper;
import io.github.artsobol.kurkod.feature.iam.entity.Role;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.iam.dto.request.LoginRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserProfileDTO;
import io.github.artsobol.kurkod.feature.iam.entity.RefreshToken;
import io.github.artsobol.kurkod.feature.iam.entity.User;
import io.github.artsobol.kurkod.exception.business.InvalidDataException;
import io.github.artsobol.kurkod.feature.iam.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.feature.iam.repository.RoleRepository;
import io.github.artsobol.kurkod.feature.iam.repository.UserRepository;
import io.github.artsobol.kurkod.infrastructure.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.infrastructure.security.validation.AccessValidator;
import io.github.artsobol.kurkod.feature.iam.entity.SystemRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
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
    public UserProfileDTO login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidDataException("auth.credentials.invalid");
        }

        User user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new InvalidDataException("auth.credentials.invalid"));

        RefreshToken refreshToken = refreshTokenService.generateOrUpdateRefreshToken(user);
        String token = jwtTokenProvider.generateToken(user);
        UserProfileDTO userProfileDTO = userMapper.toUserProfileDto(user, token, refreshToken.getToken());
        userProfileDTO.setToken(token);

        return userProfileDTO;
    }

    @Override
    public UserProfileDTO refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenService.validateAndRefreshToken(refreshTokenValue);
        User user = refreshToken.getUser();
        String accessToken = jwtTokenProvider.generateToken(user);
        return userMapper.toUserProfileDto(user,
                accessToken,
                refreshToken.getToken());
    }

    @Override
    public UserProfileDTO registerUser(@NotNull RegistrationRequest request) {
        accessValidator.validateNewUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getConfirmPassword()
        );

        Role userRole = roleRepository.findByName(SystemRole.USER.getRole())
                .orElseThrow(() -> new NotFoundException("role.not.found", SystemRole.USER.getRole()));

        User newUser = userMapper.fromDto(request);

        String enc = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(enc);
        newUser.setRoles(Set.of(userRole));
        newUser = userRepository.save(newUser);

        RefreshToken refreshToken = refreshTokenService.generateOrUpdateRefreshToken(newUser);
        String token = jwtTokenProvider.generateToken(newUser);

        return userMapper.toUserProfileDto(newUser, token, refreshToken.getToken());
    }

}
