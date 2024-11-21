package com.intern.app.services.interfaces;

import com.github.javafaker.Bool;
import com.intern.app.models.dto.datamodel.ExtendPageConfig;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.request.InstructorRequestCreationRequest;
import com.intern.app.models.dto.request.InstructorUpdateRequest;
import com.intern.app.models.dto.response.InstructorRequestResponse;
import com.intern.app.models.dto.response.InstructorResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Instructor;
import com.intern.app.models.enums.RequestStatus;

import java.util.List;

public interface IInstructorService {
    ReturnResult<Boolean> CreateInstructor(InstructorCreationRequest instructorCreationRequest);

    ReturnResult<Boolean> RequestInstructor(InstructorRequestCreationRequest instructorRequestCreationRequest);
    ReturnResult<Boolean> UpdateInstructor(InstructorUpdateRequest instructorUpdateRequest);

    ReturnResult<Boolean> SetRequestStatus(RequestStatus requestStatus, List<String> instructorRequestIds);
    ReturnResult<Boolean> CompleteRequest(List<String> instructorRequestIds);
    ReturnResult<Boolean> ClearAllStudentAvailableInstructorRequests(String instructorRequestId);
    ReturnResult<PagedData<InstructorRequestResponse, ExtendPageConfig>> GetAllInstructorRequestOfInstructorPaging(ExtendPageConfig pageConfig, String ínstructorId);
    ReturnResult<InstructorResponse> GetInstructorData(String instructorId);
    ReturnResult<InstructorResponse> GetMyInstructorData();
}
