package com.intern.app.controller;

import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.datamodel.StudentPageConfig;
import com.intern.app.models.dto.request.StudentCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.StudentResponse;
import com.intern.app.services.StudentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(path = "/api/student")
@AllArgsConstructor
public class StudentController {
    StudentService studentService;

    @PostMapping("/{studentId}")
    public ResponseEntity<ReturnResult<StudentResponse>> FindStudentById(@PathVariable String studentId) {
        ReturnResult<StudentResponse> result = studentService.FindStudentById(studentId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/MyProfile")
    public ResponseEntity<ReturnResult<StudentResponse>> FindMyProfile() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        ReturnResult<StudentResponse> result = studentService.GetStudentByUsername(username);

        return ResponseEntity.ok().body(result);
    }

//    NOT FINISH
    @PostMapping("/CreateStudent")
    public ResponseEntity<ReturnResult<Boolean>> CreateStudent(@RequestBody StudentCreationRequest studentCreationRequest) {
        ReturnResult<Boolean> result = studentService.CreateStudent(studentCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetAllStudentPaging")
    public ResponseEntity<ReturnResult<PagedData<StudentResponse, StudentPageConfig>>> GetAllStudentPaging(@RequestBody StudentPageConfig page) {
        ReturnResult<PagedData<StudentResponse, StudentPageConfig>> result = studentService.GetAllStudentPaging(page);

        return ResponseEntity.ok().body(result);
    }
}
