package io.github.artsobol.kurkod.model.request.chicken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChickenPatchRequest {
    private short weight;

    private LocalDate birthDate;

    private Integer breedId;
}
