package io.github.artsobol.kurkod.web.domain.chicken.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.chicken.mapper.ChickenMapper;
import io.github.artsobol.kurkod.web.domain.breed.error.BreedError;
import io.github.artsobol.kurkod.web.domain.chicken.error.ChickenError;
import io.github.artsobol.kurkod.web.domain.chicken.model.dto.ChickenDTO;
import io.github.artsobol.kurkod.web.domain.breed.model.entity.Breed;
import io.github.artsobol.kurkod.web.domain.chicken.model.entity.Chicken;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPatchRequest;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPutRequest;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPostRequest;
import io.github.artsobol.kurkod.web.domain.breed.repository.BreedRepository;
import io.github.artsobol.kurkod.web.domain.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.web.domain.chicken.service.api.ChickenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.artsobol.kurkod.common.util.VersionUtils.checkVersion;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChickenServiceImpl implements ChickenService {

    private final ChickenRepository chickenRepository;
    private final BreedRepository breedRepository;
    private final ChickenMapper chickenMapper;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public ChickenDTO create(ChickenPostRequest chickenPostRequest) {
        Chicken chicken = chickenMapper.toEntity(chickenPostRequest);
        chicken.setBreed(getBreedById(chickenPostRequest.getBreedId()));
        chicken = chickenRepository.save(chicken);

        log.info(ApiLogMessage.CREATE_ENTITY.getValue(),
                getCurrentUsername(),
                LogHelper.getEntityName(chicken),
                chicken.getId());
        return chickenMapper.toDto(chicken);
    }

    @Override
    public ChickenDTO get(Integer id) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Chicken.class), id);
        return chickenMapper.toDto(getChickenById(id));
    }

    @Override
    public List<ChickenDTO> getAll() {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername(), LogHelper.getEntityName(Chicken.class));
        return chickenRepository.findAll().stream()
                .map(chickenMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer id, Long version) {
        Chicken chicken = getChickenById(id);
        checkVersion(chicken.getVersion(), version);
        chicken.setActive(false);
        chickenRepository.save(chicken);
        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), chicken, id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public ChickenDTO replace(Integer id, ChickenPutRequest chickenPutRequest, Long version) {
        Chicken chicken = getChickenById(id);
        checkVersion(chicken.getVersion(), version);
        chickenMapper.updateFully(chicken, chickenPutRequest);
        Breed breed = getBreedById(chickenPutRequest.getBreedId());
        chicken.setBreed(breed);
        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), chicken, id);
        return chickenMapper.toDto(chickenRepository.save(chicken));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public ChickenDTO update(Integer id, ChickenPatchRequest chickenPatchRequest, Long version) {
        Chicken chicken = getChickenById(id);
        checkVersion(chicken.getVersion(), version);
        chickenMapper.updatePartially(chicken, chickenPatchRequest);
        if (chickenPatchRequest.getBreedId() != null) {
            Breed breed = getBreedById(chickenPatchRequest.getBreedId());
            chicken.setBreed(breed);
        }
        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), chicken, id);
        return chickenMapper.toDto(chickenRepository.save(chicken));
    }

    private Breed getBreedById(Integer id) {
        return breedRepository.findBreedById(id).orElseThrow(() ->
                new NotFoundException(BreedError.NOT_FOUND_BY_ID, id));
    }

    protected Chicken getChickenById(Integer id) {
        return chickenRepository.findChickenById(id).orElseThrow(() ->
                new NotFoundException(ChickenError.NOT_FOUND_BY_ID, id));
    }
}
