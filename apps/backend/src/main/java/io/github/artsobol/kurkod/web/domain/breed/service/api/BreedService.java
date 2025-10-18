package io.github.artsobol.kurkod.web.domain.breed.service.api;

import io.github.artsobol.kurkod.web.domain.breed.model.dto.BreedDTO;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPatchRequest;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPostRequest;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPutRequest;

import java.util.List;

public interface BreedService {

    BreedDTO create(BreedPostRequest breedPostRequest);

    BreedDTO get(Integer id);

    List<BreedDTO> getAll();

    BreedDTO replace(Integer id, BreedPutRequest breedPutRequest);

    BreedDTO update(Integer id, BreedPatchRequest breedPatchRequest);

    void delete(Integer id);
}
