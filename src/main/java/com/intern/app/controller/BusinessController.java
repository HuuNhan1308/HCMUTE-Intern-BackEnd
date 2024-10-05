package com.intern.app.controller;

import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.request.BusinessUpdateRequest;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.enums.RecruitmentRequestStatus;
import com.intern.app.services.implement.BusinessService;
import com.intern.app.services.interfaces.IBusinessService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/business")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class BusinessController {
    IBusinessService businessService;

    @PostMapping("/CreateBusiness")
    public ResponseEntity<ReturnResult<Boolean>> CreateBusiness(@RequestBody BusinessCreationRequest businessCreationRequest) {
        ReturnResult<Boolean> result = businessService.CreateBusiness(businessCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/BusinessProfile/{businessId}")
    public ResponseEntity<ReturnResult<BusinessResponse>> BusinessProfile(@PathVariable String businessId) {
        ReturnResult<BusinessResponse> result = businessService.GetBusinessProfileById(businessId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/UpdateProfile")
    public ResponseEntity<ReturnResult<Boolean>> UpdateProfile(@RequestBody BusinessUpdateRequest businessUpdateRequest) {
        ReturnResult<Boolean> result = businessService.UpdateBusinessProfile(businessUpdateRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/UpdateProfileById/{businessId}")
    public ResponseEntity<ReturnResult<Boolean>> UpdateProfileById(@PathVariable String businessId, @RequestBody BusinessUpdateRequest businessUpdateRequest) {
        ReturnResult<Boolean> result = businessService.UpdateBusinessProfileById(businessId, businessUpdateRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/SetRecruitmentRequestStatus/{recruitmentRequestId}")
    public ResponseEntity<ReturnResult<Boolean>> SetRecruitmentRequestStatus(@RequestBody RecruitmentRequestStatus recruitmentRequestStatus, @PathVariable String recruitmentRequestId) {
        ReturnResult<Boolean> result = businessService.SetRecruitmentRequestStatus(recruitmentRequestStatus, recruitmentRequestId);

        return ResponseEntity.ok().body(result);
    }
}
