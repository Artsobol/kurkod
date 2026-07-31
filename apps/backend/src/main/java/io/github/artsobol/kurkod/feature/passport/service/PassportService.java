package io.github.artsobol.kurkod.feature.passport.service;

import io.github.artsobol.kurkod.feature.passport.dto.response.PassportDTO;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportCreateRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportUpdateRequest;

public interface PassportService {

    PassportDTO get(Long workerId);

    PassportDTO create(Long workerId, PassportCreateRequest request);
    PassportDTO update(Long workerId, PassportUpdateRequest request, Long version);

    void delete(Long workerId, Long version);
}
