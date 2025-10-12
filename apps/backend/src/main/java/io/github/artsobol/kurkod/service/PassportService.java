package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.passport.PassportDTO;
import io.github.artsobol.kurkod.model.request.passport.PassportPostRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPutRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPatchRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface PassportService {

    IamResponse<PassportDTO> getPassport(@NotNull Integer workerId);

    IamResponse<PassportDTO> createPassport(@NotNull Integer workerId, @Valid PassportPostRequest passportPostRequest);

    IamResponse<PassportDTO> updateFullyPassport(@NotNull Integer workerId, @Valid PassportPutRequest passportPutRequest);

    IamResponse<PassportDTO> updatePartiallyPassport(@NotNull Integer workerId, @Valid PassportPatchRequest passportPatchRequest);

    void deletePassport(@NotNull Integer workerId);
}
