package io.github.artsobol.kurkod.feature.employmentcontract.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentContractUpdateRequest {

    @Size(min = 2, max = 20, message = "Contract number should be between 2 and 20 characters")
    private String contractNumber;

    @Positive
    private Integer salary;

    private Long staffId;

    private LocalDate startDate;

    private LocalDate endDate;
}
