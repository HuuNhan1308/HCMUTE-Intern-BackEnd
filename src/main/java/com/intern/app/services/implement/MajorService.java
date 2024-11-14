package com.intern.app.services.implement;

import com.intern.app.models.dto.request.MajorRequest;
import com.intern.app.services.interfaces.IMajorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class MajorService implements IMajorService {
    FacultyRepository facultyRepository;
    MajorRepository majorRepository;
    MajorMapper majorMapper;

    public ReturnResult<List<MajorResponse>> GetMajorsByFacultyId(String facultyId) {
        var result = new ReturnResult<List<MajorResponse>>();

        Faculty faculty = facultyRepository.findByFacultyId(facultyId)
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_EXISTED));

        List<Major> majors = majorRepository.findByFaculty(faculty);

        List<MajorResponse> majorResponse = majors.stream().map(this.majorMapper::toMajorResponse).toList();

        result.setResult(majorResponse);

        return result;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ReturnResult<Boolean> SaveMajor(MajorRequest majorRequest) {
        var result = new ReturnResult<Boolean>();

        // Check if the Faculty exists
        Faculty faculty = facultyRepository.findById(majorRequest.getFacultyId())
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_EXISTED));

        if (majorRequest.getMajorId() == null) {
            // CASE ADD
            Major major = majorMapper.toMajor(majorRequest);
            major.setFaculty(faculty); // Associate the Major with the Faculty
            majorRepository.save(major);
        } else {
            // CASE EDIT
            Major major = majorRepository.findById(majorRequest.getMajorId())
                    .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

            // Update major properties from MajorRequest
            majorMapper.updateMajor(major, majorRequest);
            major.setFaculty(faculty); // Ensure Faculty association is maintained or updated
            majorRepository.save(major);
        }

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }

}
