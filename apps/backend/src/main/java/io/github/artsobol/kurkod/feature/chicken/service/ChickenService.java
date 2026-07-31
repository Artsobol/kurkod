package io.github.artsobol.kurkod.feature.chicken.service;

import io.github.artsobol.kurkod.feature.chicken.dto.response.ChickenDTO;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenUpdateRequest;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChickenService {

    ChickenDTO create(ChickenCreateRequest request);

    ChickenDTO get(Long id);

    List<ChickenDTO> getAll();

    Page<ChickenDTO> getPage(Pageable pageable);

    void delete(Long id, Long version);
    ChickenDTO update(Long id, ChickenUpdateRequest request, Long version);
}
