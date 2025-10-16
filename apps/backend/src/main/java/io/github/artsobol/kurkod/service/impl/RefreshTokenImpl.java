package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.model.constants.ApiErrorMessage;
import io.github.artsobol.kurkod.model.entity.RefreshToken;
import io.github.artsobol.kurkod.model.entity.User;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.repository.RefreshTokenRepository;
import io.github.artsobol.kurkod.service.RefreshTokenService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateOrUpdateRefreshToken(User user) {
        return refreshTokenRepository.findByUserId(user.getId())
                .map(refreshToken -> {
                    refreshToken.setCreatedAt(LocalDateTime.now());
                    refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
                    return refreshTokenRepository.save(refreshToken);
                })
                .orElseGet(
                        () -> {
                            RefreshToken newToken = new RefreshToken();
                            newToken.setUser(user);
                            newToken.setCreatedAt(LocalDateTime.now());
                            newToken.setToken(ApiUtils.generateUuidWithoutDash());
                            return refreshTokenRepository.save(newToken);
                        }
                );
    }

    @Override
    public RefreshToken validateAndRefreshToken(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(
                        () -> new NotFoundException(ApiErrorMessage.NOT_FOUND_REFRESH_TOKEN.getMessage(requestRefreshToken))
                );

        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
        return refreshTokenRepository.save(refreshToken);
    }
}
