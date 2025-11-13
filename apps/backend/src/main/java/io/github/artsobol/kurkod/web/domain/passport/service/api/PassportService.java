package io.github.artsobol.kurkod.web.domain.passport.service.api;

import io.github.artsobol.kurkod.web.domain.passport.model.dto.PassportDTO;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPostRequest;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPutRequest;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPatchRequest;

public interface PassportService {

    PassportDTO get(Integer workerId);

    PassportDTO create(Integer workerId, PassportPostRequest request);

    PassportDTO replace(Integer workerId, PassportPutRequest request, Long version);

    PassportDTO update(Integer workerId, PassportPatchRequest request, Long version);

    void delete(Integer workerId, Long version);
}
