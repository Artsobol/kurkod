package io.github.artsobol.kurkod.web.domain.breed.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.breed.mapper.BreedMapper;
import io.github.artsobol.kurkod.web.domain.breed.error.BreedError;
import io.github.artsobol.kurkod.web.domain.breed.model.dto.BreedDTO;
import io.github.artsobol.kurkod.web.domain.breed.model.entity.Breed;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPatchRequest;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPostRequest;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPutRequest;
import io.github.artsobol.kurkod.web.domain.breed.repository.BreedRepository;
import io.github.artsobol.kurkod.web.domain.breed.service.api.BreedService;
import jakarta.validation.constraints.NotNull;
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
public class BreedServiceImpl implements BreedService {

    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public BreedDTO create(BreedPostRequest breedPostRequest) {
        Breed breed = breedMapper.toEntity(breedPostRequest);
        breed = breedRepository.save(breed);

        log.info(ApiLogMessage.CREATE_ENTITY.getValue(),
                getCurrentUsername(),
                LogHelper.getEntityName(breed),
                breed.getId());
        return breedMapper.toDto(breed);
    }

    @Override
    public BreedDTO get(@NotNull Integer id) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(Breed.class), id);
        return breedMapper.toDto(getBreedById(id));
    }

    @Override
    public List<BreedDTO> getAll() {
        log.debug(ApiLogMessage.GET_ALL_ENTITIES.getValue(), getCurrentUsername());
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

        log.info(ApiLogMessage.REPLACE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(breed),id);
        return breedMapper.toDto(breed);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public BreedDTO update(Integer id, BreedPatchRequest breedPatchRequest) {
        Breed breed = getBreedById(id);
        breedMapper.updatePartially(breed, breedPatchRequest);
        breed = breedRepository.save(breed);

        log.info(ApiLogMessage.UPDATE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(breed),id);
        return breedMapper.toDto(breed);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('DIRECTOR', 'SUPER_ADMIN')")
    public void delete(Integer id) {
        Breed breed = breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException("Breed with id " + id + " not found"));
        breed.setDeleted(true);

        log.info(ApiLogMessage.DELETE_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(breed),id);
        breedRepository.save(breed);
    }

    protected Breed getBreedById(Integer id) {
        return breedRepository.findBreedByIdAndDeletedFalse(id).orElseThrow(() ->
                new NotFoundException(BreedError.NOT_FOUND_BY_ID.format(id)));
    }

}
