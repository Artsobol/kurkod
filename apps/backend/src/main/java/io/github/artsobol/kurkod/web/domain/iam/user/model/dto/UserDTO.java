package io.github.artsobol.kurkod.web.domain.iam.user.model.dto;

import io.github.artsobol.kurkod.web.domain.iam.role.model.dto.RoleDTO;
import io.github.artsobol.kurkod.web.domain.iam.userrole.model.entity.UserRole;
import io.github.artsobol.kurkod.web.domain.iam.user.model.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private UserRole role;
    private RegistrationStatus registrationStatus;
    private List<RoleDTO> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
