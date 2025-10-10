package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.breed.BreedDTO;
import io.github.artsobol.kurkod.model.request.breed.BreedPatchRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPostRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BreedService {

    IamResponse<BreedDTO> createBreed(BreedPostRequest breedPostRequest);

    IamResponse<BreedDTO> getById(@NotNull Integer id);

    IamResponse<List<BreedDTO>> getAll();

    IamResponse<BreedDTO> updateFully(@NotNull Integer id, @Valid @RequestBody BreedPutRequest breedPutRequest);

    IamResponse<BreedDTO> updatePartially(@NotNull Integer id, @RequestBody BreedPatchRequest breedPatchRequest);

    void deleteById(@NotNull Integer id);
}
