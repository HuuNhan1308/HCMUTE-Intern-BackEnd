package com.intern.app.controller;

import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Business;
import com.intern.app.services.BusinessService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/business")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class BusinessController {
    BusinessService businessService;

    @PostMapping("/CreateBusiness")
    public ResponseEntity<ReturnResult<Boolean>> CreateBusiness(@RequestBody BusinessCreationRequest businessCreationRequest) {
        ReturnResult<Boolean> result = businessService.CreateBusiness(businessCreationRequest);

        return ResponseEntity.ok().body(result);
    }

}
