package com.intern.app.controller;

import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.implement.FacultyService;
import com.intern.app.services.interfaces.IFacultyService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/faculty")
@AllArgsConstructor
public class FacultyController {
    IFacultyService facultyService;

    @GetMapping("/GetAllFaculties")
    public ResponseEntity<ReturnResult<List<FacultyResponse>>> GetAllFaculties() {
        ReturnResult<List<FacultyResponse>> result = facultyService.GetAllFaculties();

        return ResponseEntity.ok().body(result);
    }
}
