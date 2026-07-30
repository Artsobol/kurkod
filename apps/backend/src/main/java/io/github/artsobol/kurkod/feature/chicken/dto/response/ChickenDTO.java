package io.github.artsobol.kurkod.feature.chicken.dto.response;


import io.github.artsobol.kurkod.feature.breed.dto.response.BreedDTO;
import io.github.artsobol.kurkod.feature.cage.dto.response.CageDTO;

import java.time.LocalDate;

public record ChickenDTO(
        Long id,
        String name,
        Integer weight,
        LocalDate birthDate,
        BreedDTO breed,
        CageDTO cage,
        Long version
) {
};
