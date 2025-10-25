package io.github.artsobol.kurkod.web.domain.diet.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.diet.error.DietError;
import io.github.artsobol.kurkod.web.domain.diet.mapper.DietMapper;
import io.github.artsobol.kurkod.web.domain.diet.model.dto.DietDTO;
import io.github.artsobol.kurkod.web.domain.diet.model.entity.Diet;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPatchRequest;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPostRequest;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPutRequest;
import io.github.artsobol.kurkod.web.domain.diet.repository.DietRepository;
import io.github.artsobol.kurkod.web.domain.diet.service.api.DietService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public DietDTO get(Integer id) {
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
    public DietDTO create(DietPostRequest dietPostRequest) {
        ensureNotExists(dietPostRequest.getCode());
        Diet diet = dietMapper.toEntity(dietPostRequest);
        dietRepository.save(diet);
        log.info(ApiLogMessage.CREATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(diet), diet.getId());
        return dietMapper.toDTO(diet);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public DietDTO update(Integer id, DietPatchRequest dietPatchRequest) {
        Diet diet = getDietById(id);
        dietMapper.update(diet, dietPatchRequest);
        dietRepository.save(diet);
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(diet), id);
        return dietMapper.toDTO(diet);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public DietDTO replace(Integer id, DietPutRequest dietPutRequest) {
        Diet diet = getDietById(id);
        dietMapper.replace(diet, dietPutRequest);
        diet = dietRepository.save(diet);
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(diet), id);
        return dietMapper.toDTO(diet);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer id) {
        Diet diet = getDietById(id);
        diet.setActive(false);
        dietRepository.save(diet);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(diet), id);
    }

    protected void ensureNotExists(String code) {
        if (existsByCode(code)){
            log.info(DietError.ALREADY_EXISTS.format(code));
            throw new DataExistException(DietError.ALREADY_EXISTS.format(code));
        }
    }

    protected boolean existsByCode(String code){
        return dietRepository.existsByCodeAndIsActiveTrue(code);
    }

    protected Diet getDietById(Integer id){
        return dietRepository.findById(id).orElseThrow(() -> new NotFoundException(DietError.NOT_FOUND_BY_ID.format(id)));
    }
}
