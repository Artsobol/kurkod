package io.github.artsobol.kurkod.feature.rows.service;

import io.github.artsobol.kurkod.feature.rows.dto.request.RowsCreateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.request.RowsUpdateRequest;
import io.github.artsobol.kurkod.feature.rows.dto.response.RowsResponse;
import java.util.List;

public interface RowsService {

    RowsResponse find(Long workshopId, Integer rowHumber);

    List<RowsResponse> findAll(Long workshopId);

    RowsResponse create(Long workshopId, RowsCreateRequest request);

    RowsResponse update(Long workshopId, Integer rowHumber, RowsUpdateRequest request, Long version);
    void delete(Long workshopId, Integer rowHumber, Long version);
}
