package io.github.artsobol.kurkod.web.domain.chicken.service.api;

import io.github.artsobol.kurkod.web.domain.chicken.model.dto.ChickenDTO;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPatchRequest;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPutRequest;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPostRequest;

import java.util.List;

public interface ChickenService {

    ChickenDTO create(ChickenPostRequest request);

    ChickenDTO get(Integer id);

    List<ChickenDTO> getAll();

    void delete(Integer id, Long version);

    ChickenDTO replace(Integer id, ChickenPutRequest request, Long version);

    ChickenDTO update(Integer id, ChickenPatchRequest request, Long version);
}
