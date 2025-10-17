package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.breed.BreedDTO;
import io.github.artsobol.kurkod.model.request.breed.BreedPatchRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPostRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.BreedService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/breeds", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Breeds", description = "Breed operations")
public class BreedController {

    private final BreedService breedService;


    @Operation(summary = "Get breed by ID", description = "Returns a single breed by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<BreedDTO>> getBreedById(@Parameter(
            description = "Breed identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.getById(id);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "List all breeds", description = "Returns all breeds.")
    @GetMapping
    public ResponseEntity<IamResponse<List<BreedDTO>>> getAllBreeds() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<List<BreedDTO>> response = breedService.getAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new breed", description = "Creates a new breed. Name must be unique.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<BreedDTO>> createBreed(@Valid @RequestBody BreedPostRequest breedPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.createBreed(breedPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Replace a breed", description = "Fully replaces a breed by ID.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<BreedDTO>> updateFully(@Parameter(
                                                                     description = "Breed identifier",
                                                                     example = "42"
                                                             ) @PathVariable(name = "id") Integer id,
                                                             @Valid @RequestBody BreedPutRequest breedPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.updateFully(id, breedPutRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Partially update a breed", description = "Applies a partial update to a breed by ID.")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<BreedDTO>> updatePartially(@Parameter(
                                                                         description = "Breed identifier",
                                                                         example = "42"
                                                                 ) @PathVariable(name = "id") Integer id,
                                                                 @Valid @RequestBody BreedPatchRequest breedPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.updatePartially(id, breedPatchRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a breed", description = "Deletes a breed by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Breed identifier", example = "42") @PathVariable(
            name = "id"
    ) Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        breedService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
