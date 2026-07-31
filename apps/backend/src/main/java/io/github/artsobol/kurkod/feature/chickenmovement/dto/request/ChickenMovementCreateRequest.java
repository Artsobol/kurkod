package io.github.artsobol.kurkod.feature.chickenmovement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChickenMovementCreateRequest {

    private OffsetDateTime movedAt;

    private Long fromCageId;

    @NotNull
    private Long toCageId;
}
