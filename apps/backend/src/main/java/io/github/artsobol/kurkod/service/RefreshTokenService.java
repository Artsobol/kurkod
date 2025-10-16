package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.entity.RefreshToken;
import io.github.artsobol.kurkod.model.entity.User;

public interface RefreshTokenService {

    RefreshToken generateOrUpdateRefreshToken(User user);

    RefreshToken validateAndRefreshToken(String refreshToken);
}
