package com.intern.app.controller;

import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.FacultyRequest;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.implement.FacultyService;
import com.intern.app.services.implement.PagingService;
import com.intern.app.services.interfaces.IFacultyService;
import com.intern.app.services.interfaces.IPagingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/faculty")
@AllArgsConstructor
public class FacultyController {
    IPagingService pagingService;
    IFacultyService facultyService;

    @GetMapping("/GetAllFaculties")
    public ResponseEntity<ReturnResult<List<FacultyResponse>>> GetAllFaculties() {
        ReturnResult<List<FacultyResponse>> result = facultyService.GetAllFaculties();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetFacultyPaging")
    public ResponseEntity<ReturnResult<PagedData<FacultyResponse, PageConfig>>> GetAllFaculties(@RequestBody PageConfig pageConfig) {
        ReturnResult<PagedData<FacultyResponse, PageConfig>> result = pagingService.GetFacultyPaging(pageConfig);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/SaveFaculty")
    public ResponseEntity<ReturnResult<Boolean>> SaveFaculty(@RequestBody FacultyRequest facultyRequest) {
        ReturnResult<Boolean> result = facultyService.SaveFaculty(facultyRequest);

        return ResponseEntity.ok().body(result);
    }
}
