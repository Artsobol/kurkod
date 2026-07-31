package io.github.artsobol.kurkod.feature.passport.service;

import io.github.artsobol.kurkod.feature.passport.dto.response.PassportResponse;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportCreateRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportUpdateRequest;

public interface PassportService {

    PassportResponse get(Long workerId);

    PassportResponse create(Long workerId, PassportCreateRequest request);
    PassportResponse update(Long workerId, PassportUpdateRequest request, Long version);

    void delete(Long workerId, Long version);
}
