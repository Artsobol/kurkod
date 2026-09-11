package io.github.artsobol.kurkod.feature.breed.service;

import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BreedService {

  BreedResponse create(BreedCreateRequest breedCreateRequest);

  BreedResponse get(Long id);

  List<BreedResponse> getAll();

  Page<BreedResponse> getPage(Pageable pageable);

  BreedResponse update(Long id, BreedUpdateRequest breedUpdateRequest, Long version);

  void delete(Long id, Long version);
}
