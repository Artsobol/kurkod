package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.passport.PassportDTO;
import io.github.artsobol.kurkod.model.request.passport.PassportPostRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPutRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPatchRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface PassportService {

    PassportDTO get(Integer workerId);

    PassportDTO create(Integer workerId, PassportPostRequest passportPostRequest);

    PassportDTO replace(Integer workerId, PassportPutRequest passportPutRequest);

    PassportDTO update(Integer workerId, PassportPatchRequest passportPatchRequest);

    void delete(Integer workerId);
}
