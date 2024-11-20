package com.intern.app.services.interfaces;

import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.request.BusinessUpdateRequest;
import com.intern.app.models.dto.request.RecruitmentRequestGrading;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.enums.RequestStatus;

public interface IBusinessService {
    ReturnResult<Boolean> CreateBusiness(BusinessCreationRequest businessCreationRequest);

    ReturnResult<Boolean> SetRecruitmentRequestStatus(RequestStatus requestStatus, String recruitmentRequestId);

    ReturnResult<BusinessResponse> GetBusinessData(String businessId);

    ReturnResult<Boolean> UpdateBusinessProfile(BusinessUpdateRequest businessUpdateRequest);

    ReturnResult<Boolean> UpdateBusinessProfileById(String businessId, BusinessUpdateRequest businessUpdateRequest);

    ReturnResult<BusinessResponse> GetMyBusinessData();

    ReturnResult<Boolean> GradePoint(RecruitmentRequestGrading recruitmentRequestGrading);
}
