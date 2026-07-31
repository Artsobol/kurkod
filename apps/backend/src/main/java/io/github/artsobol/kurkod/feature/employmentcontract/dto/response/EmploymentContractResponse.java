package io.github.artsobol.kurkod.feature.employmentcontract.dto.response;

import java.time.LocalDate;
import java.time.Instant;

public record EmploymentContractResponse(
    String contractNumber,
    Integer salary,
    String position,
    String firstNameWorker,
    String lastNameWorker,
    LocalDate startDate,
    LocalDate endDate,
    Instant createdAt,
    Instant updatedAt,
    Long version) {}
