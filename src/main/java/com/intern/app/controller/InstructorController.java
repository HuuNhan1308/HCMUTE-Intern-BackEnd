package com.intern.app.controller;

import com.intern.app.models.dto.datamodel.ExtendPageConfig;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.request.InstructorRequestCreationRequest;
import com.intern.app.models.dto.request.InstructorUpdateRequest;
import com.intern.app.models.dto.response.InstructorRequestResponse;
import com.intern.app.models.dto.response.InstructorResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.InstructorRequest;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.services.implement.PagingService;
import com.intern.app.services.interfaces.IInstructorService;
import com.intern.app.services.interfaces.IPagingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/instructor")
@AllArgsConstructor
public class InstructorController {
    IPagingService pagingService;
    IInstructorService instructorService;

    @PostMapping("/CreateInstructor")
    ResponseEntity<ReturnResult<Boolean>> CreateInstructor(@RequestBody InstructorCreationRequest instructorCreationRequest) {
        ReturnResult<Boolean> result = instructorService.CreateInstructor(instructorCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetInstructorPaging")
    ResponseEntity<ReturnResult<PagedData<InstructorResponse, PageConfig>>> GetInstructorPaging(@RequestBody PageConfig pageConfig) {
        ReturnResult<PagedData<InstructorResponse, PageConfig>> result = pagingService.GetInstructorsPaging(pageConfig);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/RequestInstructor")
    ResponseEntity<ReturnResult<Boolean>> RequestInstructor(@RequestBody InstructorRequestCreationRequest instructorRequestCreationRequest) {
        ReturnResult<Boolean> result = instructorService.RequestInstructor(instructorRequestCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/SetRequestStatus")
    ResponseEntity<ReturnResult<Boolean>> SetRequestStatus(@RequestParam String instructorRequestId, @RequestBody RequestStatus requestStatus) {
        ReturnResult<Boolean> result = instructorService.SetRequestStatus(requestStatus, instructorRequestId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetAllInstructorRequestOfInstructorPaging")
    ResponseEntity<ReturnResult<PagedData<InstructorRequestResponse, ExtendPageConfig>>> GetAllInstructorRequestOfInstructorPaging(@RequestBody ExtendPageConfig extendPageConfig, @RequestParam String instructorId) {
        ReturnResult<PagedData<InstructorRequestResponse, ExtendPageConfig>> result = instructorService.GetAllInstructorRequestOfInstructorPaging(extendPageConfig, instructorId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetInstructorData")
    ResponseEntity<ReturnResult<InstructorResponse>> GetAllInstructorRequestOfInstructorPaging(@RequestParam String instructorId) {
        ReturnResult<InstructorResponse> result = instructorService.GetInstructorData(instructorId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetMyInstructorData")
    ResponseEntity<ReturnResult<InstructorResponse>> GetMyInstructorData() {
        ReturnResult<InstructorResponse> result = instructorService.GetMyInstructorData();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/UpdateInstructor")
    ResponseEntity<ReturnResult<Boolean>> UpdateInstructor(@RequestBody InstructorUpdateRequest instructorUpdateRequest) {
        ReturnResult<Boolean> result = instructorService.UpdateInstructor(instructorUpdateRequest);

        return ResponseEntity.ok().body(result);
    }
}
