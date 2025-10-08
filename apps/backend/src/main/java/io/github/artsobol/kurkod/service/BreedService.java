package io.github.artsobol.kurkod.service;

import io.github.artsobol.kurkod.model.dto.Breed.BreedDTO;
import io.github.artsobol.kurkod.model.request.breed.BreedRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import jakarta.validation.constraints.NotNull;

public interface BreedService {

    IamResponse<BreedDTO> createBreed(BreedRequest breedRequest);

    IamResponse<BreedDTO> getById(@NotNull Integer id);
}
