package io.github.artsobol.kurkod.feature.user.mapper;

import io.github.artsobol.kurkod.feature.user.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
