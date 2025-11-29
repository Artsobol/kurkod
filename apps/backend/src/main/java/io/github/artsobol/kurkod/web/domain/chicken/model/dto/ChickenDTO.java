package io.github.artsobol.kurkod.web.domain.chicken.model.dto;


import io.github.artsobol.kurkod.web.domain.breed.model.dto.BreedDTO;
import io.github.artsobol.kurkod.web.domain.cage.model.dto.CageDTO;

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
