package io.github.artsobol.kurkod.feature.diet.service;

import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.exception.http.DataExistException;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.diet.error.DietError;
import io.github.artsobol.kurkod.feature.diet.mapper.DietMapper;
import io.github.artsobol.kurkod.feature.diet.dto.response.DietDTO;
import io.github.artsobol.kurkod.feature.diet.entity.Diet;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietUpdateRequest;
import io.github.artsobol.kurkod.feature.diet.dto.request.DietCreateRequest;
import io.github.artsobol.kurkod.feature.diet.repository.DietRepository;
import io.github.artsobol.kurkod.feature.diet.service.DietService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.infrastructure.util.VersionUtils.checkVersion;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;
    private final DietMapper dietMapper;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    public DietDTO get(Long id) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Diet.class), id);
        return dietMapper.toDTO(getDietById(id));
    }

    @Override
    public List<DietDTO> getAll() {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Diet.class));
        return dietRepository.findAllByIsActiveTrue()
                .stream()
                .map(dietMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public DietDTO create(DietCreateRequest request) {
        ensureNotExists(request.getCode());
        Diet diet = dietMapper.toEntity(request);
        dietRepository.save(diet);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(diet), diet.getId());
        return dietMapper.toDTO(diet);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public DietDTO update(Long id, DietUpdateRequest request, Long version) {
        Diet diet = getDietById(id);
        checkVersion(diet.getVersion(), version);
        dietMapper.update(diet, request);
        dietRepository.save(diet);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(diet), id);
        return dietMapper.toDTO(diet);
    }
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Long id, Long version) {
        Diet diet = getDietById(id);
        checkVersion(diet.getVersion(), version);
        diet.setActive(false);
        dietRepository.save(diet);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(diet), id);
    }

    protected void ensureNotExists(String code) {
        if (existsByCode(code)){
            log.info(DietError.ALREADY_EXISTS.format(code));
            throw new DataExistException("diet.already.exists", code);
        }
    }

    protected boolean existsByCode(String code){
        return dietRepository.existsByCodeAndIsActiveTrue(code);
    }

    protected Diet getDietById(Long id){
        return dietRepository.findById(id).orElseThrow(() -> new NotFoundException("diet.not.found", id));
    }
}
