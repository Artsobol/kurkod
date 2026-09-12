package io.github.artsobol.kurkod.feature.breed.service;

import static io.github.artsobol.kurkod.infrastructure.utils.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedCreateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.request.BreedUpdateRequest;
import io.github.artsobol.kurkod.feature.breed.dto.response.BreedResponse;
import io.github.artsobol.kurkod.feature.breed.entity.Breed;
import io.github.artsobol.kurkod.feature.breed.mapper.BreedMapper;
import io.github.artsobol.kurkod.feature.breed.repository.BreedRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService, BreedFinderService {

  private final BreedRepository breedRepository;
  private final BreedMapper breedMapper;

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public BreedResponse create(BreedCreateRequest breedCreateRequest) {
    log.debug("Creating breed: breedName={}", breedCreateRequest.name());

    ensureNotExists(breedCreateRequest.name());
    Breed breed =
        Breed.create(
            breedCreateRequest.name(),
            breedCreateRequest.eggsNumber(),
            breedCreateRequest.weight());
    breed = breedRepository.save(breed);

    log.info("Breed created: breedId={}", breed.getId());
    return breedMapper.toResponse(breed);
  }

  @Override
  @Transactional(readOnly = true)
  public BreedResponse getById(@NotNull Long breedId) {
    return breedMapper.toResponse(findByIdOrThrow(breedId));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<BreedResponse> getPage(Pageable pageable) {
    return breedRepository.findAllByIsActiveTrue(pageable).map(breedMapper::toResponse);
  }

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public BreedResponse update(Long breedId, BreedUpdateRequest breedUpdateRequest, Long version) {
    log.debug("Updating breed: breedId={}", breedId);

    Breed breed = findByIdOrThrow(breedId);
    checkVersion(breed.getVersion(), version);
    breed.updateDetails(
        breedUpdateRequest.name(), breedUpdateRequest.eggsNumber(), breedUpdateRequest.weight());

    log.info("Breed updated: breedId={}", breedId);
    return breedMapper.toResponse(breed);
  }

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public void delete(Long breedId, Long version) {
    log.debug("Deleting breed: breedId={} version={}", breedId, version);

    Breed breed = findByIdOrThrow(breedId);
    checkVersion(breed.getVersion(), version);
    breed.deactivate();

    log.info("Breed deleted: breedId={}", breedId);
  }

  @Override
  @Transactional(readOnly = true)
  public Breed findByIdOrThrow(Long breedId) {
    log.debug("Fetching breed: breedId={}", breedId);

    return breedRepository
        .findByIdAndIsActiveTrue(breedId)
        .orElseThrow(() -> new NotFoundException("breed.not.found", breedId));
  }

  protected void ensureNotExists(String breedName) {
    log.debug("Fetching breedName not exists: breedName={}", breedName);
    if (breedRepository.existsByNameAndIsActiveTrue(breedName)) {
      throw new DataExistException("breed.already.exists", breedName);
    }
  }
}
