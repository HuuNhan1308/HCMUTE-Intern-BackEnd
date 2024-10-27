package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.FacultyMapper;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.request.InstructorRequestCreationRequest;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.InstructorResponse;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.repository.*;
import com.intern.app.services.interfaces.IInstructorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class InstructorService implements IInstructorService {
    private static final Logger log = LoggerFactory.getLogger(InstructorService.class);
    ProfileService profileService;
    RoleRepository roleRepository;
    FacultyRepository facultyRepository;
    InstructorRepository instructorRepository;
    ProfileMapper profileMapper;
    FacultyMapper facultyMapper;
    StudentRepository studentRepository;
    InstructorRequestRepository instructorRequestRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> CreateInstructor(InstructorCreationRequest instructorCreationRequest) {
        var result = new ReturnResult<Boolean>();

        Role instructorRole = roleRepository.findByRoleName("INSTRUCTOR")
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        Profile createdProfile = profileService.CreateUser(instructorCreationRequest.getProfile(), instructorRole).getResult();

        Faculty faculty = facultyRepository.findByFacultyId(instructorCreationRequest.getFacultyId())
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_EXISTED));

        Instructor instructor = Instructor.builder()
                .faculty(faculty)
                .profile(createdProfile)
                .build();

        instructorRepository.save(instructor);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }

    public ReturnResult<List<InstructorResponse>> GetAllInstrutors() {
        var result = new ReturnResult<List<InstructorResponse>>();

        try {
            List<InstructorResponse> instructorResponses = instructorRepository.findAll().stream().map(entity -> {
                ProfileResponse profileResponse = profileMapper.toProfileResponse(entity.getProfile());
                FacultyResponse facultyResponse = facultyMapper.toFacultyResponse(entity.getFaculty());

                return InstructorResponse.builder()
                        .faculty(facultyResponse)
                        .profile(profileResponse)
                        .build();
            }).toList();

            result.setResult(instructorResponses);
            result.setCode(200);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    @PreAuthorize("hasAuthority('REQUEST_INSTRUCTOR')")
    public ReturnResult<Boolean> RequestInstructor(InstructorRequestCreationRequest instructorRequestCreationRequest) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        Student student = studentRepository.findById(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        Instructor instructor = instructorRepository.findByInstructorId(instructorRequestCreationRequest.getInstructorId())
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        InstructorRequest instructorRequest = InstructorRequest.builder()
                .student(student)
                .instructor(instructor)
                .messageToInstructor(instructorRequestCreationRequest.getMessageToInstructor())
                .instructorStatus(RequestStatus.PENDING)
                .build();

        instructorRequestRepository.save(instructorRequest);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }

    @PreAuthorize("hasAuthority('SET_INSTRUCTOR_REQUEST_STATUS')")
    public ReturnResult<Boolean> SetRequestStatus(RequestStatus requestStatus, String instructorRequestId) {
        var result = new ReturnResult<Boolean>();

        InstructorRequest instructorRequest = instructorRequestRepository.findByInstructorRequestId(instructorRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_REQUEST_NOT_FOUND));

        boolean isApproved = instructorRequestRepository.findAllByStudentStudentId(instructorRequest.getStudent().getStudentId())
                .stream()
                .anyMatch((req) -> req.getInstructorStatus().equals(RequestStatus.APPROVED));

        if(isApproved) {
            result.setResult(Boolean.FALSE);
            result.setMessage("Học sinh đã được giảng viên khác chọn, vui lòng tải lại trang");
            result.setCode(200);
        }
        else {
            instructorRequest.setInstructorStatus(requestStatus);

            instructorRequestRepository.save(instructorRequest);

            this.ClearAllStudentAvailableInstructorRequests(instructorRequestId);

            result.setResult(Boolean.TRUE);
            result.setCode(200);
        }

        return result;
    }

    @Override
    public ReturnResult<Boolean> ClearAllStudentAvailableInstructorRequests(String instructorRequestId) {
        var result = new ReturnResult<Boolean>();

        InstructorRequest instructorRequest = instructorRequestRepository.findByInstructorRequestId(instructorRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_REQUEST_NOT_FOUND));

        List<InstructorRequest> pendingInstructorRequests = instructorRequestRepository
                .findAllByStudentStudentIdAndInstructorStatus(instructorRequest.getStudent().getStudentId(), RequestStatus.PENDING);

        pendingInstructorRequests = pendingInstructorRequests.stream()
                .peek(pendingInstructorRequest -> pendingInstructorRequest.setInstructorStatus(RequestStatus.REJECT))
                .toList();

        instructorRequestRepository.saveAll(pendingInstructorRequests);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }
}
