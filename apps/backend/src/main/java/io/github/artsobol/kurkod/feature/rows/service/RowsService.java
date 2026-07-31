package io.github.artsobol.kurkod.feature.rows.service;

import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.rows.dto.response.RowsResponse;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsUpdateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RowsService {

    RowsResponse find(Long workshopId, Integer rowHumber);

    List<RowsResponse> findAll(Long workshopId);

    RowsResponse create(Long workshopId, RowsCreateRequest request);

    RowsResponse update(Long workshopId, Integer rowHumber, RowsUpdateRequest request, Long version);
    void delete(Long workshopId, Integer rowHumber, Long version);
}
