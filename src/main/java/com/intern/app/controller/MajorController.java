package com.intern.app.controller;

import com.intern.app.models.dto.response.MajorResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.MajorService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/major")
@AllArgsConstructor
public class MajorController {
    MajorService majorService;

    @GetMapping("/GetMajorsByFacultyId/{facultyId}")
    public ResponseEntity<ReturnResult<List<MajorResponse>>> GetMajorsByFacultyId(@PathVariable String facultyId) {
        ReturnResult<List<MajorResponse>> result = majorService.GetMajorsByFacultyId(facultyId);

        return ResponseEntity.ok().body(result);
    }
}
