package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.ChickenMapper;
import io.github.artsobol.kurkod.error.impl.BreedError;
import io.github.artsobol.kurkod.error.impl.ChickenError;
import io.github.artsobol.kurkod.model.dto.chicken.ChickenDTO;
import io.github.artsobol.kurkod.model.entity.Breed;
import io.github.artsobol.kurkod.model.entity.Chicken;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPatchRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPutRequest;
import io.github.artsobol.kurkod.model.request.chicken.ChickenPostRequest;
import io.github.artsobol.kurkod.repository.BreedRepository;
import io.github.artsobol.kurkod.repository.ChickenRepository;
import io.github.artsobol.kurkod.service.ChickenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChickenServiceImpl implements ChickenService {

    private final ChickenRepository chickenRepository;
    private final BreedRepository breedRepository;
    private final ChickenMapper chickenMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public ChickenDTO create(ChickenPostRequest chickenPostRequest) {
        Chicken chicken = chickenMapper.toEntity(chickenPostRequest);
        chicken.setBreed(getBreedById(chickenPostRequest.getBreedId()));
        chicken = chickenRepository.save(chicken);
        return chickenMapper.toDto(chicken);
    }

    @Override
    public ChickenDTO get(Integer id) {
        return chickenMapper.toDto(getChickenById(id));
    }

    @Override
    public List<ChickenDTO> getAll() {
        return chickenRepository.findAllByDeletedFalse().stream()
                .map(chickenMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer id) {
        Chicken chicken = getChickenById(id);
        chicken.setDeleted(true);
        chickenRepository.save(chicken);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public ChickenDTO replace(Integer id, ChickenPutRequest chickenPutRequest) {
        Chicken chicken = getChickenById(id);
        chickenMapper.updateFully(chicken, chickenPutRequest);
        Breed breed = getBreedById(chickenPutRequest.getBreedId());
        chicken.setBreed(breed);
        return chickenMapper.toDto(chickenRepository.save(chicken));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public ChickenDTO update(Integer id, ChickenPatchRequest chickenPatchRequest) {
        Chicken chicken = getChickenById(id);
        chickenMapper.updatePartially(chicken, chickenPatchRequest);
        if (chickenPatchRequest.getBreedId() != null) {
            Breed breed = getBreedById(chickenPatchRequest.getBreedId());
            chicken.setBreed(breed);
        }
        return chickenMapper.toDto(chickenRepository.save(chicken));
    }

    private Breed getBreedById(Integer id) {
        return breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException(BreedError.NOT_FOUND_BY_ID.format(id)));
    }

    protected Chicken getChickenById(Integer id) {
        return chickenRepository.findChickenByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException(ChickenError.NOT_FOUND_BY_ID.format(id)));
    }
}
