package io.github.artsobol.kurkod.feature.user.service;

import io.github.artsobol.kurkod.feature.user.dto.request.CreateUserRequest;
import io.github.artsobol.kurkod.feature.user.dto.request.UserCreateRequest;
import io.github.artsobol.kurkod.feature.user.dto.request.UserUpdateRequest;
import io.github.artsobol.kurkod.feature.user.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.user.entity.User;
import java.util.List;

public interface UserService {

  User createUser(CreateUserRequest request);

  User findActiveByEmail(String email);

  UserResponse getById(Long userId);

  List<UserResponse> getAll();

  UserResponse getByUsername(String username);

  UserResponse create(UserCreateRequest request);

  UserResponse update(Long userId, UserUpdateRequest request, Long version);

  void deleteById(Long userId, Long expectedVersion);
}
