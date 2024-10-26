package com.intern.app.services.interfaces;

import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.response.RecruitmentResponse;
import com.intern.app.models.dto.response.RecruitmentResponseShort;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;

public interface IRecruitmentService {
    ReturnResult<Boolean> CreateRecruitment(RecruitmentCreationRequest recruitmentCreationRequest);

    ReturnResult<Boolean> RequestRecruitment(RecruitmentRequestCreationRequest recruitmentRequestCreationRequest);

    ReturnResult<Boolean> ClearAllStudentAvailableRecruitmentRequests(Student student);

    ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetRecruitmentPaging(PageConfig pageConfig);

    ReturnResult<RecruitmentResponse> GetRecruitmentById(String recruitmentId);

    ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetAllBusinessRecruitmentPaging(PageConfig pageConfig);
}
