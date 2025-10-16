package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.request.user.LoginRequest;
import io.github.artsobol.kurkod.model.dto.user.UserProfileDTO;
import io.github.artsobol.kurkod.model.request.user.RegistrationUserRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;

public interface AuthService {

    IamResponse<UserProfileDTO> login(LoginRequest request);

    IamResponse<UserProfileDTO> refreshAccessToken(String refreshToken);

    IamResponse<UserProfileDTO> registerUser(RegistrationUserRequest registrationUserRequest);
}
