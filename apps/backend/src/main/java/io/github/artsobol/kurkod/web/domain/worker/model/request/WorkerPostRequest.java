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
@AllArgsConstructor
@NoArgsConstructor
public class WorkerPostRequest {

    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    private String lastName;

    @Size(max = 30, message = "Patronymic should be less then 30 characters")
    private String patronymic;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+()\\-\\s]{7,20}$",
            message = "Phone number format is invalid"
    )
    String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be less than 100 characters")
    String email;

}
