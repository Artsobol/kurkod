package io.github.artsobol.kurkod.web.domain.dismissal.service.api;

import io.github.artsobol.kurkod.web.domain.dismissal.model.dto.DismissalDTO;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPostRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPutRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPatchRequest;

import java.util.List;

public interface DismissalService {

    DismissalDTO getByWorkerAndDismissed(Integer workerId, Integer dismissedId);

    List<DismissalDTO> getAllByWorker(Integer workerId);

    List<DismissalDTO> getAllByDismissed(Integer dismissedId);

    DismissalDTO create(DismissalPostRequest request);

    DismissalDTO replace(Integer workerId, DismissalPutRequest request, Long version);

    DismissalDTO update(Integer workerId, DismissalPatchRequest request, Long version);
}
