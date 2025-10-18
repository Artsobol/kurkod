package io.github.artsobol.kurkod.web.domain.chicken.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChickenPutRequest {

    @NotNull
    private Integer weight;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Integer breedId;
}
