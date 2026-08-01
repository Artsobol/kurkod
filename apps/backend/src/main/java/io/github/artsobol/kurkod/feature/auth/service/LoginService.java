package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.feature.auth.dto.request.LoginRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;

public interface LoginService {
  AuthResponse login(LoginRequest request, SessionMetadata metadata);
}
