package io.github.artsobol.kurkod.feature.breed.service;

import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BreedService {

  BreedResponse create(BreedCreateRequest breedCreateRequest);

  BreedResponse get(Long id);

  List<BreedResponse> getAll();

  Page<BreedResponse> getPage(Pageable pageable);

  BreedResponse update(Long id, BreedUpdateRequest breedUpdateRequest, Long version);

  void delete(Long id, Long version);
}
