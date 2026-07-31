package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.dto.request.LoginRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserProfileResponse;
import io.github.artsobol.kurkod.feature.iam.dto.request.RegistrationRequest;

public interface AuthService {

  UserProfileResponse login(LoginRequest request);

  UserProfileResponse refreshAccessToken(String refreshToken);

  UserProfileResponse registerUser(RegistrationRequest registrationRequest);
}
