package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserUpdateRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserCreateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

  UserResponse getById(Long userId);

  List<UserResponse> getAll();

  UserResponse getByUsername(String username);

  UserResponse create(UserCreateRequest request);

  UserResponse update(Long userId, UserUpdateRequest request, Long version);

  void deleteById(Long userId, Long expectedVersion);
}
