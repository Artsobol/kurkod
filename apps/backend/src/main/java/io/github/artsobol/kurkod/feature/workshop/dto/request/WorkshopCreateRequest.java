package io.github.artsobol.kurkod.feature.workshop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class WorkshopCreateRequest {

    @NotNull
    @Positive
    private Integer workshopNumber;
}
