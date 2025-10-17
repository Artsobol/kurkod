package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.user.UserDTO;
import io.github.artsobol.kurkod.model.request.user.UserPatchRequest;
import io.github.artsobol.kurkod.model.request.user.UserPostRequest;
import io.github.artsobol.kurkod.model.request.user.UserPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    IamResponse<UserDTO> getUserById(Integer userId);

    IamResponse<List<UserDTO>> getAllUsers();

    IamResponse<UserDTO> getUserByUsername(String username);

    IamResponse<UserDTO> createUser(UserPostRequest userPostRequest);

    IamResponse<UserDTO> updateFullyUser(Integer userId, UserPutRequest userPutRequest);

    IamResponse<UserDTO> updatePartiallyUser(Integer userId, UserPatchRequest userPatchRequest);

    void deleteUser(Integer userId);
}
