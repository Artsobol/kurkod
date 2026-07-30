package io.github.artsobol.kurkod.feature.report.dto.response;

import java.math.BigDecimal;

public record WorkerReportDailyEggsDTO(
        Long workerId,
        String firstName,
        String lastName,
        BigDecimal avgEggsPerDay
){}
