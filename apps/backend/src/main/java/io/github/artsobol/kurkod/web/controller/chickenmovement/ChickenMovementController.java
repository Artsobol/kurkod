package io.github.artsobol.kurkod.web.controller.chickenmovement;

import io.github.artsobol.kurkod.common.util.LocationUtils;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.dto.ChickenMovementDTO;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.request.ChickenMovementPostRequest;
import io.github.artsobol.kurkod.web.domain.chickenmovement.service.api.ChickenMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Chicken Movement", description = "Operations with chicken movements (history of relocations)")
public class ChickenMovementController {

    private final ChickenMovementService chickenMovementService;

    @Operation(summary = "Get movement by ID")
    @GetMapping("/chicken-movements/{id}")
    public ResponseEntity<ChickenMovementDTO> getById(
            @PathVariable Long id) {

        ChickenMovementDTO response = chickenMovementService.get(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List movements by chicken")
    @GetMapping("/chickens/{chickenId}/movements")
    public ResponseEntity<List<ChickenMovementDTO>> getAllByChicken(
            @PathVariable Long chickenId) {

        List<ChickenMovementDTO> response = chickenMovementService.getAllByChickenId(chickenId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get current movement for chicken")
    @GetMapping("/chickens/{chickenId}/movements/current")
    public ResponseEntity<ChickenMovementDTO> getCurrent(
            @PathVariable Long chickenId) {

        ChickenMovementDTO response = chickenMovementService.getCurrentCage(chickenId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create movement for chicken")
    @PostMapping(value = "/chickens/{chickenId}/movements", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChickenMovementDTO> create(
            @PathVariable Long chickenId,
            @Valid @RequestBody ChickenMovementPostRequest request) {

        ChickenMovementDTO response = chickenMovementService.create(chickenId, request);
        return ResponseEntity.created(LocationUtils.buildLocation()).body(response);
    }
}
