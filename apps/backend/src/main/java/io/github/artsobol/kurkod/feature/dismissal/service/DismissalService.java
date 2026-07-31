package io.github.artsobol.kurkod.feature.dismissal.service;

import io.github.artsobol.kurkod.feature.dismissal.dto.response.DismissalResponse;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalCreateRequest;
import io.github.artsobol.kurkod.feature.dismissal.dto.request.DismissalUpdateRequest;

import java.util.List;

public interface DismissalService {

    DismissalResponse getByWorkerAndDismissed(Long workerId, Long dismissedId);

    List<DismissalResponse> getAllByWorker(Long workerId);

    List<DismissalResponse> getAllByDismissed(Long dismissedId);

    DismissalResponse create(DismissalCreateRequest request);
    DismissalResponse update(Long workerId, DismissalUpdateRequest request, Long version);
}
