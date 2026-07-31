package io.github.artsobol.kurkod.feature.iam.dto.response;

import io.github.artsobol.kurkod.feature.iam.entity.RegistrationStatus;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserProfileResponse {

    private Integer id;
    private String username;
    private String email;

    private RegistrationStatus registrationStatus;
    private Instant lastLogin;

    private String token;
    private String refreshToken;
    private List<RoleResponse> roles;
}
