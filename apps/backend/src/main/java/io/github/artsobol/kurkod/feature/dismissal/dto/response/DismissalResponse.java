package io.github.artsobol.kurkod.feature.dismissal.dto.response;

import java.time.LocalDate;

public record DismissalResponse(
    Integer id,
    LocalDate dismissalDate,
    String reason,
    String worker,
    String whoDismiss,
    Long version) {}
