package io.github.artsobol.kurkod.feature.breed.service;

import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.exception.http.DataExistException;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.feature.breed.mapper.BreedMapper;
import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import io.github.artsobol.kurkod.feature.breed.repository.BreedRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;
    private final BreedLookupService breedLookupService;
    private final BreedMapper breedMapper;


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public BreedResponse create(BreedCreateRequest breedCreateRequest) {
        ensureNotExists(breedCreateRequest.getName());

        Breed breed = breedMapper.toEntity(breedCreateRequest);
        breed = breedRepository.save(breed);

        return breedMapper.toResponse(breed);
    }

    @Override
    public BreedResponse get(@NotNull Long id) {
        return breedMapper.toResponse(breedLookupService.getBreedByIdOrThrow(id));
    }

    @Override
    public List<BreedResponse> getAll() {
        return breedRepository.findAllByIsActiveTrue().stream().map(breedMapper::toResponse).toList();
    }

    @Override
    public Page<BreedResponse> getPage(Pageable pageable) {
        return breedRepository.findAllByIsActiveTrue(pageable).map(breedMapper::toResponse);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public BreedResponse update(Long id, BreedUpdateRequest breedUpdateRequest, Long version) {
        Breed breed = breedLookupService.getBreedByIdOrThrow(id);
        checkVersion(breed.getVersion(), version);
        breedMapper.updatePartially(breed, breedUpdateRequest);
        breed = breedRepository.save(breed);

        return breedMapper.toResponse(breed);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long id, Long version) {
        Breed breed = breedLookupService.getBreedByIdOrThrow(id);
        checkVersion(breed.getVersion(), version);
        breed.setActive(false);

        breedRepository.save(breed);
    }

    protected void ensureNotExists(String name) {
        if (existsByName(name)) {
            throw new DataExistException("breed.already.exists", name);
        }
    }

    protected boolean existsByName(String name) {
        return breedRepository.existsByNameAndIsActiveTrue(name);
    }

}
