package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPatchRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPutRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPostRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ChickenService {

    IamResponse<ChickenDTO> createChicken(ChickenPostRequest chickenPostRequest);

    IamResponse<ChickenDTO> getById(@NotNull Integer id);

    IamResponse<List<ChickenDTO>> getAll();

    void deleteById(@NotNull Integer id);

    IamResponse<ChickenDTO> updateFully(@NotNull Integer id, @Valid @RequestBody ChickenPutRequest chickenPutRequest);

    IamResponse<ChickenDTO> updatePartially(@NotNull Integer id, @RequestBody ChickenPatchRequest chickenPatchRequest);
}
