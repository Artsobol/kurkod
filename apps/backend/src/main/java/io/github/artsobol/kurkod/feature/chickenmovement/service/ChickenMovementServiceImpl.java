package io.github.artsobol.kurkod.feature.chickenmovement.service;

import io.github.artsobol.kurkod.infrastructure.constants.ApiLogMessage;
import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.infrastructure.logging.LogHelper;
import io.github.artsobol.kurkod.infrastructure.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.feature.cage.error.CageError;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.cage.repository.CageRepository;
import io.github.artsobol.kurkod.feature.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.feature.chickenmovement.error.ChickenMovementError;
import io.github.artsobol.kurkod.feature.chickenmovement.mapper.ChickenMovementMapper;
import io.github.artsobol.kurkod.feature.chickenmovement.dto.response.ChickenMovementDTO;
import io.github.artsobol.kurkod.feature.chickenmovement.entity.ChickenMovement;
import io.github.artsobol.kurkod.feature.chickenmovement.dto.request.ChickenMovementPostRequest;
import io.github.artsobol.kurkod.feature.chickenmovement.repository.ChickenMovementRepository;
import io.github.artsobol.kurkod.feature.chickenmovement.service.ChickenMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
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
    public ChickenMovementDTO get(Long movementId) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(ChickenMovement.class), movementId);
        return chickenMovementMapper.toDto(findChickenMovementById(movementId));
    }

    @Override
    public ChickenMovementDTO getCurrentCage(Long chickenId) {
        log.debug(ApiLogMessage.GET_ENTITY.getValue(), getCurrentUsername(), LogHelper.getEntityName(ChickenMovement.class), chickenId);
        return chickenMovementMapper.toDto(chickenMovementRepository
                .findTopByChicken_IdOrderByMovedAtDesc(chickenId)
                .orElseThrow(() -> new NotFoundException("chicken.movement.not.found.by.chicken", chickenId)));
    }

    @Override
    public List<ChickenMovementDTO> getAllByChickenId(Long chickenId) {
        return chickenMovementRepository.findAllByChicken_IdOrderByMovedAtDesc(chickenId)
                .stream()
                .map(chickenMovementMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ChickenMovementDTO create(Long chickenId, ChickenMovementPostRequest request) {
        Cage fromCage = findFromCageById(request.getFromCageId());
        Cage toCage = findCageById(request.getToCageId());
        ChickenMovement chickenMovement = chickenMovementMapper.toEntity(request);
        chickenMovement.setChicken(chickenRepository.findById(chickenId)
                .orElseThrow(() -> new NotFoundException("chicken.not.found", chickenId)));
        chickenMovement.setFromCage(fromCage);
        chickenMovement.setToCage(toCage);
        chickenMovement.setMovedAt(OffsetDateTime.now());
        chickenMovement = chickenMovementRepository.save(chickenMovement);
        return chickenMovementMapper.toDto(chickenMovement);
    }

    protected ChickenMovement findChickenMovementById(Long movementId) {
        return chickenMovementRepository.findById(movementId)
                .orElseThrow(() -> new NotFoundException("chicken.movement.not.found", movementId));
    }

    protected Cage findFromCageById(Long cageId) {
        return cageId == null ? null : findCageById(cageId);
    }

    protected Cage findCageById(Long cageId) {
        return cageRepository.findById(cageId)
                .orElseThrow(() -> new NotFoundException("cage.not.found", cageId));
    }
}
