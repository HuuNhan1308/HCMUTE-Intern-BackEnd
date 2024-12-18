package com.intern.app.services.interfaces;

import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.response.*;

public interface IPagingService {
    ReturnResult<PagedData<InstructorRequestResponse, PageConfig>> GetInstructorsRequestPaging(PageConfig pageConfig);
    ReturnResult<PagedData<StudentResponse, PageConfig>> GetStudentPaging(PageConfig page);
    ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetRecruitmentPaging(PageConfig pageConfig);
    ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> GetRecruitmentRequestPaging(PageConfig pageConfig);
    ReturnResult<PagedData<InstructorResponse, PageConfig>> GetInstructorsPaging(PageConfig pageConfig);
    ReturnResult<PagedData<BusinessResponse, PageConfig>> GetBusinessPaging(PageConfig pageConfig);
    ReturnResult<PagedData<FacultyResponse, PageConfig>> GetFacultyPaging(PageConfig pageConfig);
    ReturnResult<PagedData<NotificationResponse, PageConfig>> GetNotificationPaging(PageConfig pageConfig);
    ReturnResult<PagedData<BusinessWithRecruitmentsResponse, PageConfig>> GetBusinessWithRecruitmentsPaging(PageConfig pageConfig);
    ReturnResult<PagedData<RolePermissionResponse, PageConfig>> GetRolePermissionPaging(PageConfig pageConfig);
}
