package io.github.artsobol.kurkod.web.domain.cage.service.api;

import io.github.artsobol.kurkod.web.domain.cage.model.dto.CageDTO;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePatchRequest;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePostRequest;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePutRequest;

import java.util.List;

public interface CageService {

    CageDTO find(Integer rowId, Integer cageNumber);

    List<CageDTO> findAll(Integer rowId);

    CageDTO create(Integer rowId, CagePostRequest cagePostRequest);

    CageDTO replace(Integer rowId, Integer cageNumber, CagePutRequest cagePutRequest);

    CageDTO update(Integer rowId, Integer cageNumber, CagePatchRequest cagePatchRequest);

    void delete(Integer rowId, Integer cageNumber);
}
