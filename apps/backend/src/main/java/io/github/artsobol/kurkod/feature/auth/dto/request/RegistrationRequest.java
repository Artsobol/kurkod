package io.github.artsobol.kurkod.feature.auth.dto.request;

import io.github.artsobol.kurkod.infrastructure.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@PasswordMatches(message = "auth.password.mismatch")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

  @NotBlank private String username;

  @Email @NotNull private String email;

  @NotEmpty @Size(min = 8, max = 255) private String password;

  @NotEmpty @Size(min = 8, max = 255) private String confirmPassword;
}
