package io.github.artsobol.kurkod.web.domain.iam.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank
    private String username;

    @Email
    @NotNull
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;
}
