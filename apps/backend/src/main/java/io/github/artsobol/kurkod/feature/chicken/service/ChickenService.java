package io.github.artsobol.kurkod.feature.chicken.service;

import io.github.artsobol.kurkod.feature.chicken.dto.response.ChickenDTO;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenPatchRequest;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenPutRequest;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenPostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChickenService {

    ChickenDTO create(ChickenPostRequest request);

    ChickenDTO get(Long id);

    List<ChickenDTO> getAll();

    Page<ChickenDTO> getPage(Pageable pageable);

    void delete(Long id, Long version);

    ChickenDTO replace(Long id, ChickenPutRequest request, Long version);

    ChickenDTO update(Long id, ChickenPatchRequest request, Long version);
}
