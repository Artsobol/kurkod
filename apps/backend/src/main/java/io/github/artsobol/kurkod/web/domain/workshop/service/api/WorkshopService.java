package io.github.artsobol.kurkod.web.domain.workshop.service.api;

import io.github.artsobol.kurkod.web.domain.workshop.model.dto.WorkshopDTO;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPatchRequest;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPostRequest;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPutRequest;

import java.util.List;

public interface WorkshopService {

    WorkshopDTO get(Integer id);

    List<WorkshopDTO> getAll();

    WorkshopDTO create(WorkshopPostRequest workshopPostRequest);

    WorkshopDTO update(Integer id, WorkshopPatchRequest workshopPatchRequest);

    WorkshopDTO replace(Integer id, WorkshopPutRequest workshopPutRequest);

    void delete(Integer id);
}
