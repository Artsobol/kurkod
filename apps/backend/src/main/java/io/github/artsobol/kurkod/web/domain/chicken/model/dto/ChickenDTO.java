package io.github.artsobol.kurkod.web.domain.chicken.model.dto;


import java.time.LocalDate;

public record ChickenDTO(
        Integer id,
        String name,
        Integer weight,
        LocalDate birthDate,
        Integer breedId,
        Long version
) {
};
