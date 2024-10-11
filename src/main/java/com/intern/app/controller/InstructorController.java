package com.intern.app.controller;

import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.interfaces.IFacultyService;
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
}
