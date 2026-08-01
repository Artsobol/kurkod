package io.github.artsobol.kurkod.feature.auth.service;

import io.github.artsobol.kurkod.feature.auth.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.feature.auth.dto.request.SessionMetadata;
import io.github.artsobol.kurkod.feature.auth.dto.response.AuthResponse;

public interface RegistrationService {
  AuthResponse register(RegistrationRequest request, SessionMetadata metadata);
}
