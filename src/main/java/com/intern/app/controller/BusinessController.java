package com.intern.app.controller;

import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.request.BusinessUpdateRequest;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.services.interfaces.IBusinessService;
import com.intern.app.services.interfaces.IPagingService;
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
    IPagingService pagingService;

    @PostMapping("/CreateBusiness")
    public ResponseEntity<ReturnResult<Boolean>> CreateBusiness(@RequestBody BusinessCreationRequest businessCreationRequest) {
        ReturnResult<Boolean> result = businessService.CreateBusiness(businessCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetBusinessData")
    public ResponseEntity<ReturnResult<BusinessResponse>> GetBusinessData(@RequestParam String businessId) {
        ReturnResult<BusinessResponse> result = businessService.GetBusinessData(businessId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetMyBusinessData")
    public ResponseEntity<ReturnResult<BusinessResponse>> GetMyBusinessData() {
        ReturnResult<BusinessResponse> result = businessService.GetMyBusinessData();

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

    @PostMapping("/SetRecruitmentRequestStatus")
    public ResponseEntity<ReturnResult<Boolean>> SetRecruitmentRequestStatus(@RequestBody RequestStatus requestStatus, @RequestParam String recruitmentRequestId) {
        ReturnResult<Boolean> result = businessService.SetRecruitmentRequestStatus(requestStatus, recruitmentRequestId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetBusinessPaging")
    public ResponseEntity<ReturnResult<PagedData<BusinessResponse, PageConfig>>> GetBusinessPaging(@RequestBody PageConfig pageConfig) {
        ReturnResult<PagedData<BusinessResponse, PageConfig>> result = pagingService.GetBusinessPaging(pageConfig);

        return ResponseEntity.ok().body(result);
    }

}
