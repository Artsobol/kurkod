package io.github.artsobol.kurkod.feature.passport.service;

import io.github.artsobol.kurkod.feature.passport.dto.response.PassportDTO;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPostRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPutRequest;
import io.github.artsobol.kurkod.feature.passport.dto.request.PassportPatchRequest;

public interface PassportService {

    PassportDTO get(Long workerId);

    PassportDTO create(Long workerId, PassportPostRequest request);

    PassportDTO replace(Long workerId, PassportPutRequest request, Long version);

    PassportDTO update(Long workerId, PassportPatchRequest request, Long version);

    void delete(Long workerId, Long version);
}
