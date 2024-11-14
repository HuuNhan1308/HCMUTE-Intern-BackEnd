package com.intern.app.services.interfaces;

import com.intern.app.models.dto.request.FacultyRequest;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Faculty;

import java.util.List;

public interface IFacultyService {
    ReturnResult<List<FacultyResponse>> GetAllFaculties();
    ReturnResult<Boolean> SaveFaculty(FacultyRequest facultyRequest);
}

