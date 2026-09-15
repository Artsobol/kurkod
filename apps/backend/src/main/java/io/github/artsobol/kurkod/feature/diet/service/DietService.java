package io.github.artsobol.kurkod.feature.diet.service;

import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.response.DietResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DietService {

  DietResponse getById(Long dietId);

  Page<DietResponse> getPage(Pageable pageable);

  DietResponse create(DietCreateRequest request);

  DietResponse update(Long dietId, DietUpdateRequest request, Long version);

  void delete(Long dietId, Long expectedVersion);
}
