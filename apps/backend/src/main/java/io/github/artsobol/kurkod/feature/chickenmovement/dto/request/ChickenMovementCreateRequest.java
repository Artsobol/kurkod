package io.github.artsobol.kurkod.feature.chickenmovement.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChickenMovementCreateRequest {

    private Instant movedAt;

    private Long fromCageId;

    @NotNull
    private Long toCageId;
}
