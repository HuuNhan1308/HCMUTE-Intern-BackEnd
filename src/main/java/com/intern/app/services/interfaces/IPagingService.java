package com.intern.app.services.interfaces;

import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.response.*;
import com.intern.app.models.entity.RecruitmentRequest;

public interface IPagingService {
    ReturnResult<PagedData<InstructorRequestResponse, PageConfig>> GetInstructorsRequestPaging(PageConfig pageConfig);
    ReturnResult<PagedData<StudentResponse, PageConfig>> GetStudentPaging(PageConfig page);
    ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetRecruitmentPaging(PageConfig pageConfig);
    ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> GetRecruitmentRequestPaging(PageConfig pageConfig);
    ReturnResult<PagedData<InstructorResponse, PageConfig>> GetInstructorsPaging(PageConfig pageConfig);
}
