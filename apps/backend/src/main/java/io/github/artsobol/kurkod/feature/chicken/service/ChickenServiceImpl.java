package io.github.artsobol.kurkod.feature.chicken.service;

import io.github.artsobol.kurkod.feature.breed.service.BreedLookupService;
import io.github.artsobol.kurkod.feature.cage.repository.CageRepository;
import io.github.artsobol.kurkod.feature.chicken.mapper.ChickenMapper;
import io.github.artsobol.kurkod.feature.chicken.dto.response.ChickenResponse;
import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import io.github.artsobol.kurkod.feature.chicken.entity.Chicken;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenUpdateRequest;
import io.github.artsobol.kurkod.feature.chicken.dto.request.ChickenCreateRequest;
import io.github.artsobol.kurkod.feature.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.feature.chicken.service.ChickenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChickenServiceImpl implements ChickenService {

    private final CageRepository cageRepository;
    private final ChickenRepository chickenRepository;
    private final ChickenMapper chickenMapper;
    private final BreedLookupService breedLookupService;


    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public ChickenResponse create(ChickenCreateRequest chickenCreateRequest) {
        Chicken chicken = chickenMapper.toEntity(chickenCreateRequest);
        chicken.setBreed(getBreedById(chickenCreateRequest.getBreedId()));
        chicken.setCage(cageRepository.findById(chickenCreateRequest.getCageId()).orElseThrow(() -> new NotFoundException("cage.not.found", chickenCreateRequest.getCageId())));
        chicken = chickenRepository.save(chicken);

        return chickenMapper.toResponse(chicken);
    }

    @Override
    public ChickenResponse get(Long id) {
        return chickenMapper.toResponse(getChickenById(id));
    }

    @Override
    public List<ChickenResponse> getAll() {
        return chickenRepository.findAllByIsActiveTrue().stream()
                                .map(chickenMapper::toResponse)
                                .toList();
    }

    @Override
    public Page<ChickenResponse> getPage(Pageable pageable) {
        return chickenRepository.findAllByIsActiveTrue(pageable).map(chickenMapper::toResponse);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long id, Long version) {
        Chicken chicken = getChickenById(id);
        checkVersion(chicken.getVersion(), version);
        chicken.setActive(false);
        chickenRepository.save(chicken);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public ChickenResponse update(Long id, ChickenUpdateRequest chickenUpdateRequest, Long version) {
        Chicken chicken = getChickenById(id);
        checkVersion(chicken.getVersion(), version);
        chickenMapper.updatePartially(chicken, chickenUpdateRequest);
        if (chickenUpdateRequest.getBreedId() != null) {
            Breed breed = getBreedById(chickenUpdateRequest.getBreedId());
            chicken.setBreed(breed);
        }
        if (chickenUpdateRequest.getCageId() != null) {
            chicken.setCage(cageRepository.findById(chickenUpdateRequest.getCageId()).orElseThrow(() -> new NotFoundException("cage.not.found", chickenUpdateRequest.getCageId())));
        }
        return chickenMapper.toResponse(chickenRepository.save(chicken));
    }

    private Breed getBreedById(Long id) {
        return breedLookupService.getBreedByIdOrThrow(id);
    }

    protected Chicken getChickenById(Long id) {
        return chickenRepository.findChickenByIdAndIsActiveTrue(id).orElseThrow(() ->
                new NotFoundException("chicken.not.found", id));
    }
}
