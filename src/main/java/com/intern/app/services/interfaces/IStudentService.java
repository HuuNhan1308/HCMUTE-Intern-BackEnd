package com.intern.app.services.interfaces;


import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.datamodel.StudentPageConfig;
import com.intern.app.models.dto.request.StudentCreationRequest;
import com.intern.app.models.dto.request.StudentUpdateRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.StudentResponse;


public interface IStudentService {
    ReturnResult<StudentResponse> FindStudentById(String id);
    ReturnResult<StudentResponse> GetStudentByUsername(String username);
    // NOT FINISH
    ReturnResult<Boolean> CreateStudent(StudentCreationRequest studentCreationRequest);
    ReturnResult<PagedData<StudentResponse, StudentPageConfig>> GetAllStudentPaging(PageConfig page);
    ReturnResult<Boolean> UpdateStudent(StudentUpdateRequest studentUpdateRequest);
}
