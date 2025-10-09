package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.request.chicken.ChickenRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.constraints.NotNull;

public interface ChickenService {

    IamResponse<ChickenDTO> createChicken(ChickenRequest chickenRequest);

    IamResponse<ChickenDTO> getById(@NotNull Integer id);
}
