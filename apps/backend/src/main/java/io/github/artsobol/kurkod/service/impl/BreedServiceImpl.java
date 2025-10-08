package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.BreedMapper;
import io.github.artsobol.kurkod.model.dto.Breed.BreedDTO;
import io.github.artsobol.kurkod.model.entity.Breed;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.breed.BreedRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.BreedRepository;
import io.github.artsobol.kurkod.service.BreedService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;


    @Override
    public IamResponse<BreedDTO> createBreed(BreedRequest breedRequest) {
        Breed breed = breedMapper.toEntity(breedRequest);
        breed = breedRepository.save(breed);
        return IamResponse.createSuccessful(breedMapper.toDto(breed));
    }

    @Override
    public IamResponse<BreedDTO> getById(@NotNull Integer id) {
        Breed breed = breedRepository.findById(id).orElseThrow(() -> new NotFoundException("Breed with id " + id + " not found"));
        BreedDTO breedDTO = breedMapper.toDto(breed);
        return IamResponse.createSuccessful(breedDTO);
    }
}
