package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.user.UserDTO;
import io.github.artsobol.kurkod.model.request.user.UserPatchRequest;
import io.github.artsobol.kurkod.model.request.user.UserPostRequest;
import io.github.artsobol.kurkod.model.request.user.UserPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDTO getById(Integer userId);

    List<UserDTO> getAll();

    UserDTO getByUsername(String username);

    UserDTO create(UserPostRequest userPostRequest);

    UserDTO replace(Integer userId, UserPutRequest userPutRequest);

    UserDTO update(Integer userId, UserPatchRequest userPatchRequest);

    void deleteById(Integer userId);
}
