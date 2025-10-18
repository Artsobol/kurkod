package io.github.artsobol.kurkod.web.domain.iam.user.service.api;

import io.github.artsobol.kurkod.web.domain.iam.user.model.dto.UserDTO;
import io.github.artsobol.kurkod.web.domain.iam.user.model.request.UserPatchRequest;
import io.github.artsobol.kurkod.web.domain.iam.user.model.request.UserPostRequest;
import io.github.artsobol.kurkod.web.domain.iam.user.model.request.UserPutRequest;
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
