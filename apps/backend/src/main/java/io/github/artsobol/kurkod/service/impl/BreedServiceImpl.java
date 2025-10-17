package io.github.artsobol.kurkod.service.impl;

import io.github.artsobol.kurkod.mapper.BreedMapper;
import io.github.artsobol.kurkod.error.impl.BreedError;
import io.github.artsobol.kurkod.model.dto.breed.BreedDTO;
import io.github.artsobol.kurkod.model.entity.Breed;
import io.github.artsobol.kurkod.model.exception.NotFoundException;
import io.github.artsobol.kurkod.model.request.breed.BreedPatchRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPostRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPutRequest;
import io.github.artsobol.kurkod.repository.BreedRepository;
import io.github.artsobol.kurkod.service.BreedService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public BreedDTO create(BreedPostRequest breedPostRequest) {
        Breed breed = breedMapper.toEntity(breedPostRequest);
        breed = breedRepository.save(breed);
        return breedMapper.toDto(breed);
    }

    @Override
    public BreedDTO get(@NotNull Integer id) {
        Breed breed = getBreedById(id);
        return breedMapper.toDto(breed);
    }

    @Override
    public List<BreedDTO> getAll() {
        return breedRepository.findAllByDeletedFalse().stream()
                .map(breedMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public BreedDTO replace(Integer id, BreedPutRequest breedPutRequest) {
        Breed breed = getBreedById(id);
        breedMapper.updateFully(breed, breedPutRequest);
        breed = breedRepository.save(breed);
        return breedMapper.toDto(breed);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public BreedDTO update(Integer id, BreedPatchRequest breedPatchRequest) {
        Breed breed = getBreedById(id);
        breedMapper.updatePartially(breed, breedPatchRequest);
        breed = breedRepository.save(breed);
        return breedMapper.toDto(breed);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer id) {
        Breed breed = breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Breed with id " + id + " not found"));
        breed.setDeleted(true);
        breedRepository.save(breed);
    }

    protected Breed getBreedById(Integer id) {
        return breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException(BreedError.NOT_FOUND_BY_ID.format(id)));
    }

}
