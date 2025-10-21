package io.github.artsobol.kurkod.web.domain.rows.service.api;

import io.github.artsobol.kurkod.web.domain.rows.model.dto.RowsDTO;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPatchRequest;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPostRequest;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPutRequest;

import java.util.List;

public interface RowsService {

    RowsDTO find(Integer workshopId, Integer rowHumber);

    List<RowsDTO> findAll(Integer workshopId);

    RowsDTO create(Integer workshopId, RowsPostRequest request);

    RowsDTO update(Integer workshopId, Integer rowHumber, RowsPatchRequest rowsPatchRequest);

    RowsDTO replace(Integer workshopId, Integer rowHumber, RowsPutRequest rowsPutRequest);

    void delete(Integer workshopId, Integer rowHumber);
}
