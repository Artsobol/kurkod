package io.github.artsobol.kurkod.web.domain.chickenmovement.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChickenMovementDTO {

    private Integer id;

    private Integer chickenId;

    private Integer fromCageId;

    private Integer toCageId;

    private LocalDateTime movedAt;
}
