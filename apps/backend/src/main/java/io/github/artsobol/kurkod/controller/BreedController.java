package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.constants.ApiLogMessage;
import io.github.artsobol.kurkod.model.dto.Breed.BreedDTO;
import io.github.artsobol.kurkod.model.request.breed.BreedRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.BreedService;
import io.github.artsobol.kurkod.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public ResponseEntity<IamResponse<BreedDTO>> createBreed(@RequestBody BreedRequest breedRequest) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());
        IamResponse<BreedDTO> response = breedService.createBreed(breedRequest);
        return ResponseEntity.ok(response);
    }
}
