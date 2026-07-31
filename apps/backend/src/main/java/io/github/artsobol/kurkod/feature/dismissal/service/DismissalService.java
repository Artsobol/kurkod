package io.github.artsobol.kurkod.feature.dismissal.service;

import io.github.artsobol.kurkod.feature.dismissal.dto.response.DismissalDTO;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalCreateRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalUpdateRequest;

import java.util.List;

public interface DismissalService {

    DismissalDTO getByWorkerAndDismissed(Long workerId, Long dismissedId);

    List<DismissalDTO> getAllByWorker(Long workerId);

    List<DismissalDTO> getAllByDismissed(Long dismissedId);

    DismissalDTO create(DismissalCreateRequest request);
    DismissalDTO update(Long workerId, DismissalUpdateRequest request, Long version);
}
