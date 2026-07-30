package io.github.artsobol.kurkod.feature.worker.dto.response;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageDTO;

import java.util.Set;

public record WorkerDTO(
        Long id,
        String firstName,
        String lastName,
        String patronymic,
        String phoneNumber,
        String email,
        Set<CageDTO> cages,
        Long version
) {
}