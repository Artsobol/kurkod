package io.github.artsobol.kurkod.model.request.chicken;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChickenPostRequest implements Serializable {

    @NotNull
    private short weight;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Integer breedId;
}
