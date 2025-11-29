package io.github.artsobol.kurkod.web.domain.worker.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerPatchRequest {

    private String firstName;

    private String lastName;

    private String patronymic;

    @Pattern(
            regexp = "^[0-9+()\\-\\s]{7,20}$",
            message = "Phone number format is invalid"
    )
    String phoneNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    String email;
}
