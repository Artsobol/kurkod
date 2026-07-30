package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.entity.RefreshToken;
import io.github.artsobol.kurkod.feature.iam.entity.User;

public interface RefreshTokenService {

    RefreshToken generateOrUpdateRefreshToken(User user);

    RefreshToken validateAndRefreshToken(String refreshToken);
}
