package io.github.artsobol.kurkod.feature.chickenmovement.service;

import io.github.artsobol.kurkod.exception.http.NotFoundException;
import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import io.github.artsobol.kurkod.feature.cage.repository.CageRepository;
import io.github.artsobol.kurkod.feature.chicken.repository.ChickenRepository;
import io.github.artsobol.kurkod.feature.chickenmovement.mapper.ChickenMovementMapper;
import io.github.artsobol.kurkod.feature.chickenmovement.dto.response.ChickenMovementDTO;
import io.github.artsobol.kurkod.feature.chickenmovement.entity.ChickenMovement;
import io.github.artsobol.kurkod.feature.chickenmovement.dto.request.ChickenMovementCreateRequest;
import io.github.artsobol.kurkod.feature.chickenmovement.repository.ChickenMovementRepository;
import io.github.artsobol.kurkod.feature.chickenmovement.service.ChickenMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChickenMovementServiceImpl implements ChickenMovementService {

    private final ChickenMovementRepository chickenMovementRepository;
    private final ChickenMovementMapper chickenMovementMapper;
    private final ChickenRepository chickenRepository;
    private final CageRepository cageRepository;


    @Override
    public ChickenMovementDTO get(Long movementId) {
        return chickenMovementMapper.toDto(findChickenMovementById(movementId));
    }

    @Override
    public ChickenMovementDTO getCurrentCage(Long chickenId) {
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
    public ChickenMovementDTO create(Long chickenId, ChickenMovementCreateRequest request) {
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
