package io.github.artsobol.kurkod.feature.iam.mapper;

import io.github.artsobol.kurkod.feature.iam.dto.response.RoleDTO;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserDTO;
import io.github.artsobol.kurkod.feature.iam.dto.response.UserProfileDTO;
import io.github.artsobol.kurkod.feature.iam.entity.Role;
import io.github.artsobol.kurkod.feature.iam.entity.User;
import io.github.artsobol.kurkod.feature.iam.dto.request.RegistrationRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserUpdateRequest;
import io.github.artsobol.kurkod.feature.iam.dto.request.UserCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserDTO toDto(User user);

    User toEntity(UserCreateRequest userCreateRequest);
    void updatePartially(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "token", source = "token")
    @Mapping(target =  "refreshToken", source = "refreshToken")
    UserProfileDTO toUserProfileDto(User user, String token, String refreshToken);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "registrationStatus", expression = "java(RegistrationStatus.PENDING_CONFIRMATION)")
    User fromDto(RegistrationRequest registrationRequest);

    default List<RoleDTO> mapRoles(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .toList();
    }
}
