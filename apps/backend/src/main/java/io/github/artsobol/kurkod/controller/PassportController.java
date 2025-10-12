package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.dto.passport.PassportDTO;
import io.github.artsobol.kurkod.model.request.passport.PassportPatchRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPostRequest;
import io.github.artsobol.kurkod.model.request.passport.PassportPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.PassportService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workers/{workerId}/passport")
public class PassportController {

    private final PassportService passportService;

    @GetMapping
    public ResponseEntity<IamResponse<PassportDTO>> getPassportById(@PathVariable(name = "workerId") Integer workerId){
        IamResponse<PassportDTO> response = passportService.getPassport(workerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<IamResponse<PassportDTO>> createPassport(@PathVariable(name = "workerId") Integer workerId, @RequestBody @Valid PassportPostRequest passportPostRequest){
        IamResponse<PassportDTO> response = passportService.createPassport(workerId, passportPostRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<IamResponse<PassportDTO>> updateFullyPassport(@PathVariable(name = "workerId") Integer workerId, @RequestBody @Valid PassportPutRequest passportPutRequest){
        IamResponse<PassportDTO> response = passportService.updateFullyPassport(workerId, passportPutRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<IamResponse<PassportDTO>> updatePartiallyPassport(@PathVariable(name = "workerId") Integer workerId, @RequestBody PassportPatchRequest passportPatchRequest){
        IamResponse<PassportDTO> response = passportService.updatePartiallyPassport(workerId, passportPatchRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePassport(@PathVariable(name = "workerId") Integer workerId){
        passportService.deletePassport(workerId);
        return ResponseEntity.noContent().build();
    }

}
