package io.github.artsobol.kurkod.feature.iam.dto.response;

import io.github.artsobol.kurkod.feature.iam.dto.response.RoleDTO;
import io.github.artsobol.kurkod.feature.iam.entity.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserProfileDTO {

    private Integer id;
    private String username;
    private String email;

    private RegistrationStatus registrationStatus;
    private OffsetDateTime lastLogin;

    private String token;
    private String refreshToken;
    private List<RoleDTO> roles;
}
