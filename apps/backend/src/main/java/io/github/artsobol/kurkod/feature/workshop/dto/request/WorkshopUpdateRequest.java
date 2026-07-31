package io.github.artsobol.kurkod.feature.workshop.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class WorkshopUpdateRequest {

    @Positive
    private Integer workshopNumber;
}
