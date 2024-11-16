package com.intern.app.services.interfaces;

import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.request.RecruitmentUpdateRequest;
import com.intern.app.models.dto.response.RecruitmentRequestResponse;
import com.intern.app.models.dto.response.RecruitmentResponse;
import com.intern.app.models.dto.response.RecruitmentResponseShort;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;

public interface IRecruitmentService {
    ReturnResult<Boolean> CreateRecruitment(RecruitmentCreationRequest recruitmentCreationRequest);

    ReturnResult<Boolean> RequestRecruitment(RecruitmentRequestCreationRequest recruitmentRequestCreationRequest);

    ReturnResult<Boolean> ClearAllStudentAvailableRecruitmentRequests(Student student);

    ReturnResult<RecruitmentResponse> GetRecruitmentById(String recruitmentId);

    ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetAllBusinessRecruitmentPaging(PageConfig pageConfig, String businessId);

    ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetOpenRecruitmentPaging(PageConfig pageConfig);
    ReturnResult<Boolean> UpdateRecruitment(RecruitmentUpdateRequest recruitmentUpdateRequest, String businessId);
    ReturnResult<Boolean> RejectAllRecruitmentRequest(Recruitment recruitment);
    ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> GetAllRecruitmentRequestOfRecruitmentPaging(PageConfig pageConfig, String recruitmentId);

    ReturnResult<Boolean> InviteStudent(String recruitmentId, String studentId);
}
