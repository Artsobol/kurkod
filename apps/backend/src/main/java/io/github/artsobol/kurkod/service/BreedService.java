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

    BreedDTO create(BreedPostRequest breedPostRequest);

    BreedDTO get(Integer id);

    List<BreedDTO> getAll();

    BreedDTO replace(Integer id, BreedPutRequest breedPutRequest);

    BreedDTO update(Integer id, BreedPatchRequest breedPatchRequest);

    void delete(Integer id);
}
