package io.github.artsobol.kurkod.web.domain.chickenmovement.service.impl;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.logging.LogHelper;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.web.domain.cage.error.CageError;
import io.github.artsobol.kurkod.web.domain.cage.model.entity.Cage;
import io.github.artsobol.kurkod.web.domain.cage.repository.CageRepository;
import io.github.artsobol.kurkod.web.domain.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.web.domain.chickenmovement.error.ChickenMovementError;
import io.github.artsobol.kurkod.web.domain.chickenmovement.mapper.ChickenMovementMapper;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.dto.ChickenMovementDTO;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.entity.ChickenMovement;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.request.ChickenMovementPostRequest;
import io.github.artsobol.kurkod.web.domain.chickenmovement.repository.ChickenMovementRepository;
import io.github.artsobol.kurkod.web.domain.chickenmovement.service.api.ChickenMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChickenMovementServiceImpl implements ChickenMovementService {

    private final ChickenMovementRepository chickenMovementRepository;
    private final ChickenMovementMapper chickenMovementMapper;
    private final ChickenRepository chickenRepository;
    private final CageRepository cageRepository;
    private final SecurityContextFacade securityContextFacade;

    private String getCurrentUsername() {
        return securityContextFacade.getCurrentUsername();
    }

    @Override
    public ChickenMovementDTO get(Integer movementId) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(ChickenMovement.class), movementId);
        return chickenMovementMapper.toDto(findChickenMovementById(movementId));
    }

    @Override
    public ChickenMovementDTO getCurrentCage(Integer chickenId) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(ChickenMovement.class), chickenId);
        return chickenMovementMapper.toDto(chickenMovementRepository
                .findTopByChicken_IdOrderByMovedAtDesc(chickenId)
                .orElseThrow( () -> new NotFoundException(ChickenMovementError.NOT_FOUND_BY_ID.format(chickenId))));
    }

    @Override
    public List<ChickenMovementDTO> getAllByChickenId(Integer chickenId) {
        return chickenMovementRepository.findAllByChicken_IdOrderByMovedAtDesc(chickenId)
                .stream()
                .map(chickenMovementMapper::toDto)
                .toList();
    }

    @Override
    public ChickenMovementDTO create(Integer chickenId, ChickenMovementPostRequest request) {
        Cage fromCage = findCageById(request.getFromCageId());
        Cage toCage = findCageById(request.getToCageId());
        ChickenMovement chickenMovement = chickenMovementMapper.toEntity(request);
        chickenMovement.setChicken(chickenRepository.findById(chickenId)
                .orElseThrow(() -> new NotFoundException(ChickenMovementError.NOT_FOUND_BY_ID.format(chickenId))));
        chickenMovement.setFromCage(fromCage);
        chickenMovement.setToCage(toCage);
        chickenMovement.setMovedAt(LocalDateTime.now());
        chickenMovement = chickenMovementRepository.save(chickenMovement);
        return chickenMovementMapper.toDto(chickenMovement);
    }

    protected ChickenMovement findChickenMovementById(Integer movementId) {
        return chickenMovementRepository.findById(movementId)
                .orElseThrow(() -> new NotFoundException(ChickenMovementError.NOT_FOUND_BY_ID.format(movementId)));
    }

    protected Cage findCageById(Integer cageId) {
        return cageRepository.findById(cageId)
                .orElseThrow(() -> new NotFoundException(CageError.NOT_FOUND_BY_ID.format(cageId)));
    }
}
