package io.github.artsobol.kurkod.feature.employmentcontract.dto.response;


import java.time.LocalDate;
import java.time.OffsetDateTime;

public record EmploymentContractResponse(
        String contractNumber,
        Integer salary,
        String position,
        String firstNameWorker,
        String lastNameWorker,
        LocalDate startDate,
        LocalDate endDate,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long version
) {
};
