package io.github.artsobol.kurkod.feature.diet.service;

import io.github.artsobol.kurkod.feature.diet.dto.response.DietDTO;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;

import java.util.List;

public interface DietService {

    DietDTO get(Long id);

    List<DietDTO> getAll();

    DietDTO create(DietCreateRequest request);

    DietDTO update(Long id, DietUpdateRequest request, Long version);
    void delete(Long id, Long expectedVersion);
}
