package com.intern.app.controller;


import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.response.RecruitmentResponse;
import com.intern.app.models.dto.response.RecruitmentResponseShort;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.implement.RecruitmentService;
import com.intern.app.services.interfaces.IRecruitmentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruitment")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RecruitmentController {
    IRecruitmentService recruitmentService;

    @PostMapping("/CreateRecruitment")
    public ResponseEntity<ReturnResult<Boolean>> CreateRecruitment(@RequestBody RecruitmentCreationRequest recruitmentCreationRequest) {
        ReturnResult<Boolean> result = recruitmentService.CreateRecruitment(recruitmentCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/RequestRecruitment")
    public ResponseEntity<ReturnResult<Boolean>> RequestRecruitment(@RequestBody RecruitmentRequestCreationRequest recruitmentRequestCreationRequest) {
        ReturnResult<Boolean> result = recruitmentService.RequestRecruitment(recruitmentRequestCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetRecruitmentPaging")
    public ResponseEntity<ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>>> GetRecruitmentPaging(@RequestBody PageConfig pageConfig) {
        ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> result = recruitmentService.GetRecruitmentPaging(pageConfig);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetRecruitmentById")
    public ResponseEntity<ReturnResult<RecruitmentResponse>> GetRecruitmentById(@RequestParam String recruitmentId) {
        ReturnResult<RecruitmentResponse> result = recruitmentService.GetRecruitmentById(recruitmentId);

        return ResponseEntity.ok().body(result);
    }
}
