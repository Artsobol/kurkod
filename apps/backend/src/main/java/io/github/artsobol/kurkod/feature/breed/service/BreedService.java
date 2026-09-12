package io.github.artsobol.kurkod.feature.breed.service;

import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BreedService {

  BreedResponse create(BreedCreateRequest breedCreateRequest);

  BreedResponse getById(Long breedId);

  Page<BreedResponse> getPage(Pageable pageable);

  BreedResponse update(Long breedId, BreedUpdateRequest breedUpdateRequest, Long version);

  void delete(Long breedId, Long version);
}
