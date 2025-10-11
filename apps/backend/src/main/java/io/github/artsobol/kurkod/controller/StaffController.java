package io.github.artsobol.kurkod.controller;

import io.github.artsobol.kurkod.model.dto.staff.StaffDTO;
import io.github.artsobol.kurkod.model.request.staff.StaffPatchRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPostRequest;
import io.github.artsobol.kurkod.model.request.staff.StaffPutRequest;
import io.github.artsobol.kurkod.model.response.IamResponse;
import io.github.artsobol.kurkod.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping("/{staffId}")
    public ResponseEntity<IamResponse<StaffDTO>> getStaffInfo(@PathVariable(name = "staffId") Integer staffId){
        IamResponse<StaffDTO> response = staffService.getStaffInfo(staffId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<IamResponse<List<StaffDTO>>> getAllStaffs(){
        IamResponse<List<StaffDTO>> response = staffService.getAllStaffs();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<IamResponse<StaffDTO>> createStaff(@RequestBody StaffPostRequest staffPostRequest){
        IamResponse<StaffDTO> response = staffService.createStaff(staffPostRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<IamResponse<StaffDTO>> updateFullyStaff(@PathVariable(name = "staffId") Integer staffId, @RequestBody StaffPutRequest staffPutRequest){
        IamResponse<StaffDTO> response = staffService.updateFullyStaff(staffId, staffPutRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{staffId}")
    public ResponseEntity<IamResponse<StaffDTO>> updatePartiallyStaff(@PathVariable(name = "staffId") Integer staffId, @RequestBody StaffPatchRequest staffPatchRequest){
        IamResponse<StaffDTO> response = staffService.updatePartiallyStaff(staffId, staffPatchRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable(name = "staffId") Integer staffId){
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }
}
