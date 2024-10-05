package com.intern.app.services.interfaces;

import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.request.BusinessUpdateRequest;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.enums.RecruitmentRequestStatus;

public interface IBusinessService {
    ReturnResult<Boolean> CreateBusiness(BusinessCreationRequest businessCreationRequest);

    ReturnResult<Boolean> SetRecruitmentRequestStatus(RecruitmentRequestStatus recruitmentRequestStatus, String recruitmentRequestId);

    ReturnResult<BusinessResponse> GetBusinessProfileById(String businessId);

    ReturnResult<Boolean> UpdateBusinessProfile(BusinessUpdateRequest businessUpdateRequest);

    ReturnResult<Boolean> UpdateBusinessProfileById(String businessId, BusinessUpdateRequest businessUpdateRequest);
}
