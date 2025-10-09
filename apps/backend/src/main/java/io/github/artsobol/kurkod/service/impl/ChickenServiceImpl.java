package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.BreedMapper;
import io.github.artsobol.kurkod.mapper.ChickenMapper;
import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.entity.Breed;
import io.github.artsobol.kurkod.model.entity.Chicken;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.chicken.ChickenRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.BreedRepository;
import io.github.artsobol.kurkod.repository.ChickenRepository;
import io.github.artsobol.kurkod.service.ChickenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChickenServiceImpl implements ChickenService {

    private final ChickenRepository chickenRepository;
    private final BreedRepository breedRepository;
    private final ChickenMapper chickenMapper;
    private final BreedMapper breedMapper;

    @Override
    public IamResponse<ChickenDTO> createChicken(ChickenRequest chickenRequest) {
        Chicken chicken = buildChicken(chickenRequest, getBreedById(chickenRequest.getBreedId()));
        chicken = chickenRepository.save(chicken);
        return IamResponse.createSuccessful(chickenMapper.toDto(chicken));

    }

    @Override
    public IamResponse<ChickenDTO> getById(Integer id) {
        Chicken chicken = chickenRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Chicken with id " + id + " not found"));
        return IamResponse.createSuccessful(buildChickenDTO(chicken));
    }

    private Breed getBreedById(Integer id) {
        return breedRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Breed with id " + id + " not found"));
    }

    private Chicken buildChicken(ChickenRequest chickenRequest, Breed breed) {
        Chicken chicken = chickenMapper.toEntity(chickenRequest);
        chicken.setBreed(breed);
        return chicken;
    }

    private ChickenDTO buildChickenDTO(Chicken chicken) {
        ChickenDTO chickenDTO = chickenMapper.toDto(chicken);
        chickenDTO.setBreed(breedMapper.toDto(chicken.getBreed()));
        return chickenDTO;
    }
}
