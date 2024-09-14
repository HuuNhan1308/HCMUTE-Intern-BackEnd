package com.intern.app.controller;

import com.intern.app.models.dto.request.TokenRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.StudentResponse;
import com.intern.app.services.StudentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(path = "/api/student")
@AllArgsConstructor
public class StudentController {
    StudentService studentService;

    @PostMapping("/{studentId}")
    public ResponseEntity<ReturnResult<StudentResponse>> FindStudentById(@PathVariable Long studentId) {
        ReturnResult<StudentResponse> result = studentService.FindStudentById(studentId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/MyProfile")
    public ResponseEntity<ReturnResult<StudentResponse>> FindMyProfile(@RequestBody TokenRequest token) throws ParseException {
        ReturnResult<StudentResponse> result = studentService.FindStudentByAccessToken(token.getToken());

        return ResponseEntity.ok().body(result);
    }
}
