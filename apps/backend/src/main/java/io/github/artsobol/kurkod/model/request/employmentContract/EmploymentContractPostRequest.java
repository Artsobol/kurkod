package io.github.artsobol.kurkod.model.request.employmentContract;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentContractPostRequest {

    @NotBlank
    @Size(min = 2, max = 20, message = "Contract number should be between 2 and 20 characters")
    private String contractNumber;

    @NotNull
    @Positive
    private Integer salary;

    @NotNull
    private Integer staffId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
