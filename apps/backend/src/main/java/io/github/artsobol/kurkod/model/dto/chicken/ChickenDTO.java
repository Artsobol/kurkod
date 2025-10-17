package io.github.artsobol.kurkod.model.dto.chicken;

import io.github.artsobol.kurkod.model.dto.breed.BreedDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChickenDTO implements Serializable {

    private Integer id;

    private Integer weight;

    private LocalDate birthDate;

    private Integer breedId;
}
