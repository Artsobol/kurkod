package io.github.artsobol.kurkod.feature.diet.service;

import io.github.artsobol.kurkod.feature.diet.dto.response.DietResponse;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;

import java.util.List;

public interface DietService {

    DietResponse get(Long id);

    List<DietResponse> getAll();

    DietResponse create(DietCreateRequest request);

    DietResponse update(Long id, DietUpdateRequest request, Long version);
    void delete(Long id, Long expectedVersion);
}
