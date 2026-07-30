package io.github.artsobol.kurkod.feature.iam.service;

import io.github.artsobol.kurkod.feature.iam.dto.response.UserDTO;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserPatchRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserPostRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserPutRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDTO getById(Long userId);

    List<UserDTO> getAll();

    UserDTO getByUsername(String username);

    UserDTO create(UserPostRequest request);

    UserDTO replace(Long userId, UserPutRequest request, Long version);

    UserDTO update(Long userId, UserPatchRequest request, Long version);

    void deleteById(Long userId, Long expectedVersion);
}
