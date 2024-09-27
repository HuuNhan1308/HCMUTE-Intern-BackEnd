package com.intern.app.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.MajorMapper;
import com.intern.app.models.dto.response.MajorResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Major;
import com.intern.app.repository.FacultyRepository;
import com.intern.app.repository.MajorRepository;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class MajorService {
    FacultyRepository facultyRepository;
    MajorRepository majorRepository;
    MajorMapper majorMapper;

    public ReturnResult<List<MajorResponse>> GetMajorsByFacultyId(String facultyId) throws AppException {
        var result = new ReturnResult<List<MajorResponse>>();

        Faculty faculty = facultyRepository.findByFacultyId(facultyId)
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_EXISTED));

        List<Major> majors = majorRepository.findByFaculty(faculty);

        List<MajorResponse> majorResponse = majors.stream().map(this.majorMapper::toMajorResponse).toList();

        result.setResult(majorResponse);

        return result;
    }
}
