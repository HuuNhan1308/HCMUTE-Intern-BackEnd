package com.intern.app.controller;

import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.request.InstructorRequestCreationRequest;
import com.intern.app.models.dto.response.InstructorResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.InstructorRequest;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.services.interfaces.IInstructorService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/instructor")
@AllArgsConstructor
public class InstructorController {
    IInstructorService instructorService;

    @PostMapping("/CreateInstructor")
    ResponseEntity<ReturnResult<Boolean>> CreateInstructor(@RequestBody InstructorCreationRequest instructorCreationRequest) {
        ReturnResult<Boolean> result = instructorService.CreateInstructor(instructorCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetAllInstructor")
    ResponseEntity<ReturnResult<List<InstructorResponse>>> GetAllInstructor() {
        ReturnResult<List<InstructorResponse>> result = instructorService.GetAllInstrutors();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/RequestInstructor")
    ResponseEntity<ReturnResult<Boolean>> RequestInstructor(@RequestBody InstructorRequestCreationRequest instructorRequestCreationRequest) {
        ReturnResult<Boolean> result = instructorService.RequestInstructor(instructorRequestCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/SetRequestStatus/{instructorRequestId}")
    ResponseEntity<ReturnResult<Boolean>> SetRequestStatus(@PathVariable String instructorRequestId, @RequestBody RequestStatus requestStatus) {
        ReturnResult<Boolean> result = instructorService.SetRequestStatus(requestStatus, instructorRequestId);

        return ResponseEntity.ok().body(result);
    }

}
