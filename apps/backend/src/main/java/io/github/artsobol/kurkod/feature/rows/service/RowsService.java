package io.github.artsobol.kurkod.feature.rows.service;

import io.github.artsobol.kurkod.feature.breed.dto.response.BreedDTO;
import io.github.artsobol.kurkod.feature.rows.dto.response.RowsDTO;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsUpdateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RowsService {

    RowsDTO find(Long workshopId, Integer rowHumber);

    List<RowsDTO> findAll(Long workshopId);

    RowsDTO create(Long workshopId, RowsCreateRequest request);

    RowsDTO update(Long workshopId, Integer rowHumber, RowsUpdateRequest request, Long version);
    void delete(Long workshopId, Integer rowHumber, Long version);
}
