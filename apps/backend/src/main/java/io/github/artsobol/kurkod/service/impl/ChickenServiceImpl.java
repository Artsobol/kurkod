package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.ChickenMapper;
import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.entity.Breed;
import io.github.artsobol.kurkod.model.entity.Chicken;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPatchRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPutRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPostRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.repository.BreedRepository;
import io.github.artsobol.kurkod.repository.ChickenRepository;
import io.github.artsobol.kurkod.security.validation.AccessValidator;
import io.github.artsobol.kurkod.service.ChickenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChickenServiceImpl implements ChickenService {

    private final ChickenRepository chickenRepository;
    private final BreedRepository breedRepository;
    private final ChickenMapper chickenMapper;
    private final AccessValidator accessValidator;

    @Override
    public IamResponse<ChickenDTO> createChicken(ChickenPostRequest chickenPostRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Chicken chicken = chickenMapper.toEntity(chickenPostRequest);
        chicken.setBreed(getBreedById(chickenPostRequest.getBreedId()));
        chicken = chickenRepository.save(chicken);
        return IamResponse.createSuccessful(chickenMapper.toDto(chicken));
    }

    @Override
    public IamResponse<ChickenDTO> getById(Integer id) {
        Chicken chicken = chickenRepository.findChickenByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Chicken with id " + id + " not found"));
        return IamResponse.createSuccessful(chickenMapper.toDto(chicken));
    }

    @Override
    public IamResponse<List<ChickenDTO>> getAll() {
        List<ChickenDTO> chickens = chickenRepository.findAllByDeletedFalse().stream()
                .map(chickenMapper::toDto)
                .toList();
       return IamResponse.createSuccessful(chickens);
    }

    @Override
    public void deleteById(Integer id) {
        accessValidator.validateDirectorOrSuperAdmin();

        Chicken chicken = chickenRepository.findChickenByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Chicken with id " + id + " not found"));
        chicken.setDeleted(true);
        chickenRepository.save(chicken);
    }

    @Override
    public IamResponse<ChickenDTO> updateFully(Integer id, ChickenPutRequest chickenPutRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Chicken chicken = chickenRepository.findChickenByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Chicken with id " + id + " not found"));
        chickenMapper.updateFully(chicken, chickenPutRequest);
        Breed breed = getBreedById(chickenPutRequest.getBreedId());
        chicken.setBreed(breed);
        return IamResponse.createSuccessful(chickenMapper.toDto(chickenRepository.save(chicken)));
    }

    @Override
    public IamResponse<ChickenDTO> updatePartially(Integer id, ChickenPatchRequest chickenPatchRequest) {
        accessValidator.validateDirectorOrSuperAdmin();

        Chicken chicken = chickenRepository.findChickenByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Chicken with id " + id + " not found"));
        chickenMapper.updatePartially(chicken, chickenPatchRequest);
        if (chickenPatchRequest.getBreedId() != null) {
            Breed breed = getBreedById(chickenPatchRequest.getBreedId());
            chicken.setBreed(breed);
        }
        return IamResponse.createSuccessful(chickenMapper.toDto(chickenRepository.save(chicken)));
    }

    private Breed getBreedById(Integer id) {
        return breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Breed with id " + id + " not found"));
    }
}
