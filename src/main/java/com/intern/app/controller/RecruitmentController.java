package com.intern.app.controller;


import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.RecruitmentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruitment")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RecruitmentController {
    RecruitmentService recruitmentService;

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

}
