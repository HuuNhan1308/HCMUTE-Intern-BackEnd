package com.intern.app.services.interfaces;


import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.datamodel.StudentPageConfig;
import com.intern.app.models.dto.request.StudentCreationRequest;
import com.intern.app.models.dto.request.StudentUpdateRequest;
import com.intern.app.models.dto.response.InstructorRequestResponse;
import com.intern.app.models.dto.response.RecruitmentRequestResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.StudentResponse;

import java.util.List;


public interface IStudentService {
    ReturnResult<StudentResponse> FindStudentById(String id);
    ReturnResult<StudentResponse> GetStudentByUsername(String username);
    ReturnResult<Boolean> CreateStudent(StudentCreationRequest studentCreationRequest);
    ReturnResult<Boolean> UpdateStudent(StudentUpdateRequest studentUpdateRequest);
    ReturnResult<Boolean> DeleteInstructorRequests(List<String> instructorRequestIds);
    ReturnResult<Boolean> DeleteRecruitmentRequests(List<String> recruitmentRequestIds);

    ReturnResult<PagedData<InstructorRequestResponse, PageConfig>> GetAllStudentInstructorsRequestPaging(PageConfig pageConfig);
    ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> GetAllStudentRecruitmentsRequestPaging(PageConfig pageConfig);
    ReturnResult<PagedData<StudentResponse, PageConfig>> GetAllStudentWithSeekingIntern(PageConfig pageConfig);
}
