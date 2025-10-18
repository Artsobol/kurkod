package io.github.artsobol.kurkod.web.domain.passport.service.api;

import io.github.artsobol.kurkod.web.domain.passport.model.dto.PassportDTO;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPostRequest;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPutRequest;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPatchRequest;

public interface PassportService {

    PassportDTO get(Integer workerId);

    PassportDTO create(Integer workerId, PassportPostRequest passportPostRequest);

    PassportDTO replace(Integer workerId, PassportPutRequest passportPutRequest);

    PassportDTO update(Integer workerId, PassportPatchRequest passportPatchRequest);

    void delete(Integer workerId);
}
