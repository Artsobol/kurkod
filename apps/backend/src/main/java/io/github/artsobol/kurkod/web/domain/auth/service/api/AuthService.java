package io.github.artsobol.kurkod.web.domain.auth.service.api;

import io.github.artsobol.kurkod.web.domain.iam.auth.model.request.LoginRequest;
import io.github.artsobol.kurkod.web.domain.iam.user.model.dto.UserProfileDTO;
import io.github.artsobol.kurkod.web.domain.iam.auth.model.request.RegistrationRequest;

public interface AuthService {

    UserProfileDTO login(LoginRequest request);

    UserProfileDTO refreshAccessToken(String refreshToken);

    UserProfileDTO registerUser(RegistrationRequest registrationRequest);
}
