package io.github.artsobol.kurkod.web.domain.chicken.model.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChickenPatchRequest {

    // TODO запретить пустую строку
    private String name;

    @Positive
    private Integer weight;

    @Past
    private LocalDate birthDate;

    private Integer breedId;

    private Integer cageId;
}
