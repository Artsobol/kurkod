package io.github.artsobol.kurkod.feature.worker.dto.response;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;

import java.util.Set;

public record WorkerResponse(
    Long id,
    String firstName,
    String lastName,
    String patronymic,
    String phoneNumber,
    String email,
    Set<CageResponse> cages,
    Long version) {}
