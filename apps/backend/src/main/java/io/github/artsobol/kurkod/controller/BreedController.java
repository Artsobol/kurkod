package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.breed.BreedDTO;
import io.github.artsobol.kurkod.model.request.breed.BreedPatchRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPostRequest;
import io.github.artsobol.kurkod.model.request.breed.BreedPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.BreedService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/breed")
@RequiredArgsConstructor
public class BreedController {

    private final BreedService breedService;

    @GetMapping("/{id}")
    public ResponseEntity<IamResponse<BreedDTO>> getBreedById(@PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<IamResponse<List<BreedDTO>>> getAllBreeds() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<List<BreedDTO>> response = breedService.getAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<IamResponse<BreedDTO>> createBreed(@RequestBody BreedPostRequest breedPostRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.createBreed(breedPostRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IamResponse<BreedDTO>> updateFully(@PathVariable(name = "id") Integer id, @RequestBody BreedPutRequest breedPutRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.updateFully(id, breedPutRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<IamResponse<BreedDTO>> updatePartially(@PathVariable(name = "id") Integer id, @RequestBody BreedPatchRequest breedPatchRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.updatePartially(id, breedPatchRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") Integer id) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        breedService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
