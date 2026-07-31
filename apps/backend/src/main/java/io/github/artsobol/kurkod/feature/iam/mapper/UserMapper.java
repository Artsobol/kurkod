package io.github.artsobol.kurkod.feature.iam.mapper;

import io.github.artsobol.kurkod.feature.iam.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserCreateRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserUpdateRequest;
import io.github.artsobol.kurkod.feature.iam.dto.response.RoleResponse;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserProfileResponse;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserResponse;
import io.github.artsobol.kurkod.feature.iam.entity.Role;
import io.github.artsobol.kurkod.feature.iam.entity.User;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserResponse toResponse(User user);

    User toEntity(UserCreateRequest userCreateRequest);
    void updatePartially(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "token", source = "token")
    @Mapping(target =  "refreshToken", source = "refreshToken")
    UserProfileResponse toUserProfileResponse(User user, String token, String refreshToken);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "registrationStatus", expression = "java(RegistrationStatus.PENDING_CONFIRMATION)")
    User toEntity(RegistrationRequest registrationRequest);

    default List<RoleResponse> mapRoles(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new RoleResponse(role.getId(), role.getName()))
                .toList();
    }
}
