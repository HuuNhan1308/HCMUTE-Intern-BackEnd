package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.request.FacultyRequest;
import com.intern.app.services.interfaces.IFacultyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.intern.app.mapper.FacultyMapper;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Faculty;
import com.intern.app.repository.FacultyRepository;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class FacultyService implements IFacultyService {
    FacultyRepository facultyRepository;
    FacultyMapper facultyMapper;

    public ReturnResult<List<FacultyResponse>> GetAllFaculties() {
        var result = new ReturnResult<List<FacultyResponse>>();
        List<Faculty> faculties = facultyRepository.findAll();

        List<FacultyResponse> facultyResponses = faculties.stream().map(this.facultyMapper::toFacultyResponse).toList();

        result.setResult(facultyResponses);

        return result;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ReturnResult<Boolean> SaveFaculty(FacultyRequest facultyRequest) {
        var result = new ReturnResult<Boolean>();

        if(!facultyRepository.existsById(facultyRequest.getFacultyId())) {
            //CASE ADD
            Faculty faculty = facultyMapper.toFaculty(facultyRequest);
            facultyRepository.save(faculty);
        }
        else {
            //CASE EDIT
            Faculty faculty = facultyRepository.findById(facultyRequest.getFacultyId())
                    .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_EXISTED));

            facultyMapper.updateFaculty(faculty, facultyRequest);
            facultyRepository.save(faculty);
        }

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }
}
