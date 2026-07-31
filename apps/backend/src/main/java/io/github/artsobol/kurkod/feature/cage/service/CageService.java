package io.github.artsobol.kurkod.feature.cage.service;

import io.github.artsobol.kurkod.feature.cage.dto.response.CageResponse;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageUpdateRequest;
import io.github.artsobol.kurkod.feature.cage.dto.request.CageCreateRequest;

import java.util.List;

public interface CageService {

  CageResponse find(Long rowId, Integer cageNumber);

  List<CageResponse> findAll(Long rowId);

  CageResponse create(Long rowId, CageCreateRequest request);

  CageResponse update(
      Long rowId, Integer cageNumber, CageUpdateRequest request, Long expectedVersion);

  void delete(Long rowId, Integer cageNumber, Long expectedVersion);
}
