package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.BreedMapper;
import io.github.artsobol.kurkod.model.dto.breed.BreedDTO;
import io.github.artsobol.kurkod.model.entity.Breed;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.breed.BreedPatchRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPostRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.BreedRepository;
import io.github.artsobol.kurkod.service.BreedService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;


    @Override
    public IamResponse<BreedDTO> createBreed(BreedPostRequest breedPostRequest) {
        Breed breed = breedMapper.toEntity(breedPostRequest);
        breed = breedRepository.save(breed);
        return IamResponse.createSuccessful(breedMapper.toDto(breed));
    }

    @Override
    public IamResponse<BreedDTO> getById(@NotNull Integer id) {
        Breed breed = breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundException("Breed with id " + id + " not found"));
        BreedDTO breedDTO = breedMapper.toDto(breed);
        return IamResponse.createSuccessful(breedDTO);
    }

    @Override
    public IamResponse<List<BreedDTO>> getAll() {
        List<BreedDTO> breeds = breedRepository.findAllByDeletedFalse().stream()
                .map(breedMapper::toDto)
                .toList();
        return IamResponse.createSuccessful(breeds);
    }

    @Override
    public IamResponse<BreedDTO> updateFully(Integer id, BreedPutRequest breedPutRequest) {
        Breed breed = breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Breed with id " + id + " not found"));
        breedMapper.updateFully(breed, breedPutRequest);
        breed = breedRepository.save(breed);
        return IamResponse.createSuccessful(breedMapper.toDto(breed));
    }

    @Override
    public IamResponse<BreedDTO> updatePartially(Integer id, BreedPatchRequest breedPatchRequest) {
        Breed breed = breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Breed with id " + id + " not found"));
        breedMapper.updatePartially(breed, breedPatchRequest);
        breed = breedRepository.save(breed);
        return IamResponse.createSuccessful(breedMapper.toDto(breed));
    }

    @Override
    public void deleteById(Integer id) {
        Breed breed = breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Breed with id " + id + " not found"));
        breed.setDeleted(true);
        breedRepository.save(breed);
    }
}
