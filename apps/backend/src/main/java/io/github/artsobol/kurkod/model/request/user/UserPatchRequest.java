package io.github.artsobol.kurkod.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchRequest {
    @Size(max = 30, message = "Username should be less than 30 characters")
    private String username;

    @Size(max = 255)
    private String password;

    @Email
    @Size(max = 80)
    private String email;
}
