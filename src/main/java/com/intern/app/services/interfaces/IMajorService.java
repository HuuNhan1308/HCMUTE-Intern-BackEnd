package com.intern.app.services.interfaces;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.request.MajorRequest;
import com.intern.app.models.dto.response.MajorResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Major;

import java.util.List;

public interface IMajorService {
    ReturnResult<List<MajorResponse>> GetMajorsByFacultyId(String facultyId);
    ReturnResult<Boolean> SaveMajor(MajorRequest majorRequest);
}
