package io.github.artsobol.kurkod.web.domain.chickenmovement.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChickenMovementPostRequest {

    private LocalDateTime movedAt;

    private Integer fromCageId;

    @NotNull
    private Integer toCageId;
}
