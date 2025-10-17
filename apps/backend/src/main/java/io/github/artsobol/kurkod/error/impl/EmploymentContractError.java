package io.github.artsobol.kurkod.error.impl;

import io.github.artsobol.kurkod.error.ErrorDescriptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EmploymentContractError implements ErrorDescriptor {
    NOT_FOUND_BY_ID("EMP-404", "employmentContract.employment_contract_not_found_by_id", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String messageKey;
    private final HttpStatus status;

    public String format(Object... args) {
        return String.format("[%s] %s", code, String.format(messageKey, args));
    }
}
