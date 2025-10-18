package io.github.artsobol.kurkod.web.controller.breed;

import io.github.artsobol.kurkod.common.constants.ApiLogMessage;
import io.github.artsobol.kurkod.common.util.LogUtils;
import io.github.artsobol.kurkod.web.domain.breed.model.dto.BreedDTO;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPatchRequest;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPostRequest;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPutRequest;
import io.github.artsobol.kurkod.web.response.IamResponse;
import io.github.artsobol.kurkod.web.domain.breed.service.api.BreedService;
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
    public ResponseEntity<IamResponse<BreedDTO>> get(@Parameter(
            description = "Breed identifier",
            example = "42"
    ) @PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        BreedDTO response = breedService.get(id);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }


    @Operation(summary = "List all breeds", description = "Returns all breeds.")
    @GetMapping
    public ResponseEntity<IamResponse<List<BreedDTO>>> getAllBreeds() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        List<BreedDTO> response = breedService.getAll();
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Create a new breed", description = "Creates a new breed. Name must be unique.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<BreedDTO>> createBreed(@Valid @RequestBody BreedPostRequest breedPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        BreedDTO response = breedService.create(breedPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(IamResponse.createSuccessful(response));
    }


    @Operation(summary = "Replace a breed", description = "Fully replaces a breed by ID.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<BreedDTO>> updateFully(@Parameter(
                                                                     description = "Breed identifier",
                                                                     example = "42"
                                                             ) @PathVariable(name = "id") Integer id,
                                                             @Valid @RequestBody BreedPutRequest breedPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        BreedDTO response = breedService.replace(id, breedPutRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Partially update a breed", description = "Applies a partial update to a breed by ID.")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IamResponse<BreedDTO>> updatePartially(@Parameter(
                                                                         description = "Breed identifier",
                                                                         example = "42"
                                                                 ) @PathVariable(name = "id") Integer id,
                                                                 @Valid @RequestBody BreedPatchRequest breedPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        BreedDTO response = breedService.update(id, breedPatchRequest);
        return ResponseEntity.ok(IamResponse.createSuccessful(response));
    }

    @Operation(summary = "Delete a breed", description = "Deletes a breed by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "Breed identifier", example = "42") @PathVariable(
            name = "id"
    ) Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), LogUtils.getMethodName());
        breedService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
