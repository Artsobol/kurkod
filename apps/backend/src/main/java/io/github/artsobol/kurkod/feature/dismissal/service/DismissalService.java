package io.github.artsobol.kurkod.feature.dismissal.service;

import io.github.artsobol.kurkod.feature.dismissal.dto.response.DismissalDTO;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalPostRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalPutRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalPatchRequest;

import java.util.List;

public interface DismissalService {

    DismissalDTO getByWorkerAndDismissed(Long workerId, Long dismissedId);

    List<DismissalDTO> getAllByWorker(Long workerId);

    List<DismissalDTO> getAllByDismissed(Long dismissedId);

    DismissalDTO create(DismissalPostRequest request);

    DismissalDTO replace(Long workerId, DismissalPutRequest request, Long version);

    DismissalDTO update(Long workerId, DismissalPatchRequest request, Long version);
}
