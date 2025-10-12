package io.github.artsobol.kurkod.model.dto.employmentContract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentContractDTO {

    private String contractNumber;

    private Integer salary;

    private String position;

    private String firstNameWorker;

    private String lastNameWorker;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
