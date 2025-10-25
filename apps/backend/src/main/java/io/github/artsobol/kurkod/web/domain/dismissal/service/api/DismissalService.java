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

    DismissalDTO create(DismissalPostRequest dismissalPostRequest);

    DismissalDTO replace(Integer workerId, DismissalPutRequest dismissalPutRequest);

    DismissalDTO update(Integer workerId, DismissalPatchRequest dismissalPatchRequest);
}
