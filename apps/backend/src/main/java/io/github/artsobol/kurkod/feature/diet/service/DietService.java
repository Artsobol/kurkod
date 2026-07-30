package io.github.artsobol.kurkod.feature.diet.service;

import io.github.artsobol.kurkod.feature.diet.dto.response.DietDTO;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietPatchRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietPostRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietPutRequest;

import java.util.List;

public interface DietService {

    DietDTO get(Long id);

    List<DietDTO> getAll();

    DietDTO create(DietPostRequest request);

    DietDTO update(Long id, DietPatchRequest request, Long version);

    DietDTO replace(Long id, DietPutRequest request, Long version);

    void delete(Long id, Long expectedVersion);
}
