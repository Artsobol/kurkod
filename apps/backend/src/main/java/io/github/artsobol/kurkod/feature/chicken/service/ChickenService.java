package io.github.artsobol.kurkod.feature.chicken.service;

import io.github.artsobol.kurkod.feature.chicken.dto.response.ChickenResponse;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenUpdateRequest;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChickenService {

  ChickenResponse create(ChickenCreateRequest request);

  ChickenResponse get(Long id);

  List<ChickenResponse> getAll();

  Page<ChickenResponse> getPage(Pageable pageable);

  void delete(Long id, Long version);

  ChickenResponse update(Long id, ChickenUpdateRequest request, Long version);
}
