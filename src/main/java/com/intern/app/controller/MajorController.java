package com.intern.app.controller;

import com.intern.app.models.dto.request.MajorRequest;
import com.intern.app.models.dto.response.MajorResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.implement.MajorService;

import com.intern.app.services.interfaces.IMajorService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/major")
@AllArgsConstructor
public class MajorController {
    IMajorService majorService;

    @GetMapping("/GetMajorsByFacultyId/{facultyId}")
    public ResponseEntity<ReturnResult<List<MajorResponse>>> GetMajorsByFacultyId(@PathVariable String facultyId) {
        ReturnResult<List<MajorResponse>> result = majorService.GetMajorsByFacultyId(facultyId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/SaveMajor")
    public ResponseEntity<ReturnResult<Boolean>> SaveMajor(@RequestBody MajorRequest majorRequest) {
        ReturnResult<Boolean> result = majorService.SaveMajor(majorRequest);

        return ResponseEntity.ok().body(result);
    }
}
