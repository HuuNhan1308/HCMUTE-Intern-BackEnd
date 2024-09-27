package com.intern.app.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import com.intern.app.mapper.FacultyMapper;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.StudentResponse;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Major;
import com.intern.app.repository.FacultyRepository;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class FacultyService {
    FacultyRepository facultyRepository;
    FacultyMapper facultyMapper;

    public ReturnResult<List<FacultyResponse>> GetAllFaculties() {
        var result = new ReturnResult<List<FacultyResponse>>();
        List<Faculty> faculties = facultyRepository.findAll();

        List<FacultyResponse> facultyResponses = faculties.stream().map(this.facultyMapper::toFacultyResponse).toList();

        result.setResult(facultyResponses);

        return result;
    }
}
