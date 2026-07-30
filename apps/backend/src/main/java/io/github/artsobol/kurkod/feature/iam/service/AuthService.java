package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.dto.request.LoginRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserProfileDTO;
import io.github.artsobol.kurkod.feature.iam.dto.request.RegistrationRequest;

public interface AuthService {

    UserProfileDTO login(LoginRequest request);

    UserProfileDTO refreshAccessToken(String refreshToken);

    UserProfileDTO registerUser(RegistrationRequest registrationRequest);
}
