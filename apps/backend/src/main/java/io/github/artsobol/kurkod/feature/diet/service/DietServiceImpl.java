package io.github.artsobol.kurkod.feature.diet.service;

import static io.github.artsobol.kurkod.infrastructure.utils.VersionUtils.checkVersion;

import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.response.DietResponse;
import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import io.github.artsobol.kurkod.feature.diet.mapper.DietMapper;
import io.github.artsobol.kurkod.feature.diet.repository.DietRepository;
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
public class DietServiceImpl implements DietService, DietFinderService {

  private final DietRepository dietRepository;
  private final DietMapper dietMapper;

  @Override
  public DietResponse getById(Long dietId) {
    return dietMapper.toResponse(findByIdOrThrow(dietId));
  }

  @Override
  public Page<DietResponse> getPage(Pageable pageable) {
    log.debug(
        "Fetching diets: active=true, paged={}, page={}, size={}, offset={}, sort=[{}]",
        pageable.isPaged(),
        pageable.isPaged() ? pageable.getPageNumber() : null,
        pageable.isPaged() ? pageable.getPageSize() : null,
        pageable.isPaged() ? pageable.getOffset() : null,
        pageable.getSort());
    return dietRepository.findAllByIsActiveTrue(pageable).map(dietMapper::toResponse);
  }

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public DietResponse create(DietCreateRequest request) {
    log.debug("Creating diet: dietCode={}", request.code());
    ensureNotExists(request.code());

    Diet diet = Diet.create(request.title(), request.description(), request.code(), request.season());
    dietRepository.save(diet);

    log.info("Diet created: dietId={} dietCode={}", diet.getId(), request.code());
    return dietMapper.toResponse(diet);
  }

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public DietResponse update(Long dietId, DietUpdateRequest request, Long version) {
    log.debug("Updating diet: dietId={}", dietId);
    Diet diet = findByIdOrThrow(dietId);

    checkVersion(diet.getVersion(), version);
    diet.updateDetails(request.title(), request.description(), request.code(), request.season());
    dietRepository.save(diet);

    log.info("Diet updated: dietId={}", dietId);
    return dietMapper.toResponse(diet);
  }

  @Override
  @Transactional
  @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
  public void delete(Long dietId, Long version) {
    log.debug("Deleting diet: dietId={}", dietId);
    Diet diet = findByIdOrThrow(dietId);
    checkVersion(diet.getVersion(), version);
    diet.deactivate();
    dietRepository.save(diet);
    log.info("Diet deleted: dietId={}", dietId);
  }

  @Override
  public Diet findByIdOrThrow(Long dietId) {
    log.debug("Fetching diet: dietId={}", dietId);
    return dietRepository
      .findByIdAndIsActiveTrue(dietId)
      .orElseThrow(() -> new NotFoundException("diet.not.found", dietId));
  }

  protected void ensureNotExists(String code) {
    log.debug("Fetching dietCode not exists: dietCode={}", code);
    if (existsByCode(code)) {
      throw new DataExistException("diet.already.exists", code);
    }
  }

  protected boolean existsByCode(String code) {
    log.debug("Checking if diet exists: dietCode={}", code);
    return dietRepository.existsByCodeAndIsActiveTrue(code);
  }
}
