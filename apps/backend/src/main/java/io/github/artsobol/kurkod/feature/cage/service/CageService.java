package io.github.artsobol.kurkod.feature.cage.service;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageDTO;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageUpdateRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageCreateRequest;

import java.util.List;

public interface CageService {

    CageDTO find(Long rowId, Integer cageNumber);

    List<CageDTO> findAll(Long rowId);

    CageDTO create(Long rowId, CageCreateRequest request);
    CageDTO update(Long rowId, Integer cageNumber, CageUpdateRequest request, Long expectedVersion);

    void delete(Long rowId, Integer cageNumber, Long expectedVersion);
}
