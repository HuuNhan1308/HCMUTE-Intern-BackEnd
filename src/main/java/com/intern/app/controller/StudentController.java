package com.intern.app.controller;

import com.intern.app.exception.AppException;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.StudentCreationRequest;
import com.intern.app.models.dto.request.StudentUpdateRequest;
import com.intern.app.models.dto.response.InstructorRequestResponse;
import com.intern.app.models.dto.response.RecruitmentRequestResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.StudentResponse;

import com.intern.app.services.interfaces.IExcelUploadService;
import com.intern.app.services.interfaces.IPagingService;
import com.intern.app.services.interfaces.IStudentService;
import com.intern.app.services.interfaces.IUploadService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(path = "/api/student")
@AllArgsConstructor
public class StudentController {
    IExcelUploadService excelUploadService;
    IPagingService pagingService;
    IStudentService studentService;
    IUploadService uploadService;

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

    // NOT FINISH
    @PostMapping("/CreateStudent")
    public ResponseEntity<ReturnResult<Boolean>> CreateStudent(
            @RequestBody StudentCreationRequest studentCreationRequest) {
        ReturnResult<Boolean> result = studentService.CreateStudent(studentCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetStudentPaging")
    public ResponseEntity<ReturnResult<PagedData<StudentResponse, PageConfig>>> GetStudentPaging(
            @RequestBody PageConfig page) {
        ReturnResult<PagedData<StudentResponse, PageConfig>> result = pagingService.GetStudentPaging(page);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/UpdateStudent")
    public ResponseEntity<ReturnResult<Boolean>> UpdateStudent(@RequestBody StudentUpdateRequest studentUpdateRequest) {
        ReturnResult<Boolean> result = studentService.UpdateStudent(studentUpdateRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/UploadCV")
    public ResponseEntity<ReturnResult<Boolean>> UploadCV(@RequestParam("cv") MultipartFile cv) throws AppException {
        ReturnResult<Boolean> result = uploadService.UploadCV(cv);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/ImportStudents")
    public ResponseEntity<ReturnResult<Boolean>> ImportStudents(@RequestParam("file")MultipartFile file) {
        ReturnResult<Boolean> result = excelUploadService.ImportStudents(file);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("GetAllStudentInstructorsRequestPaging")
    public ResponseEntity<ReturnResult<PagedData<InstructorRequestResponse, PageConfig>>> GetAllStudentInstructorsRequestPaging(@RequestBody PageConfig page) {
        ReturnResult<PagedData<InstructorRequestResponse, PageConfig>> result = studentService.GetAllStudentInstructorsRequestPaging(page);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("GetAllStudentRecruitmentsRequestPaging")
    public ResponseEntity<ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>>> GetAllStudentRecruitmentsRequestPaging(@RequestBody PageConfig page) {
        ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> result = studentService.GetAllStudentRecruitmentsRequestPaging(page);

        return ResponseEntity.ok().body(result);
    }
}
