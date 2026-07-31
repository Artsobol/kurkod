package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.dto.response.UserDTO;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserUpdateRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserCreateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDTO getById(Long userId);

    List<UserDTO> getAll();

    UserDTO getByUsername(String username);

    UserDTO create(UserCreateRequest request);
    UserDTO update(Long userId, UserUpdateRequest request, Long version);

    void deleteById(Long userId, Long expectedVersion);
}
