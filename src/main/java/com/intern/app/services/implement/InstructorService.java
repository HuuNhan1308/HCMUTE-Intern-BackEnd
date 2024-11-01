package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.FacultyMapper;
import com.intern.app.mapper.InstructorRequestMapper;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.models.dto.datamodel.FilterMapping;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.request.InstructorRequestCreationRequest;
import com.intern.app.models.dto.response.*;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.FilterOperator;
import com.intern.app.models.enums.FilterType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class InstructorService implements IInstructorService {
    ProfileService profileService;
    RoleRepository roleRepository;
    FacultyRepository facultyRepository;
    InstructorRepository instructorRepository;
    InstructorRequestRepository instructorRequestRepository;
    StudentRepository studentRepository;

    InstructorRequestMapper instructorRequestMapper;
    ProfileRepository profileRepository;
    private final PagingService pagingService;

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

    @PreAuthorize("hasAuthority('REQUEST_INSTRUCTOR')")
    public ReturnResult<Boolean> RequestInstructor(InstructorRequestCreationRequest instructorRequestCreationRequest) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        Student student = studentRepository.findById(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        Instructor instructor = instructorRepository.findByInstructorId(instructorRequestCreationRequest.getInstructorId())
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));



        if(instructorRequestCreationRequest.getInstructorRequestId() == null) {
            // CASE ADD
            InstructorRequest instructorRequest = instructorRequestRepository
                    .findByStudentStudentIdAndInstructorInstructorIdAndInstructorStatus(
                            student.getStudentId(),
                            instructor.getInstructorId(),
                            RequestStatus.PENDING
                    )
                    .orElse(null);

            if(instructorRequest != null && instructorRequest.getInstructorStatus() == RequestStatus.PENDING) {
                result.setResult(Boolean.FALSE);
                result.setMessage("Bạn đã gửi yêu cầu cho giảng viên này rồi, vui lòng chờ đợi phản hồi");

            } else {
                instructorRequest = InstructorRequest.builder()
                        .student(student)
                        .instructor(instructor)
                        .messageToInstructor(instructorRequestCreationRequest.getMessageToInstructor())
                        .instructorStatus(RequestStatus.PENDING)
                        .build();

                instructorRequestRepository.save(instructorRequest);

                result.setResult(Boolean.TRUE);
            }
        } else {
            // CASE EDIT
            InstructorRequest instructorRequest = instructorRequestRepository
                    .findByInstructorRequestId(instructorRequestCreationRequest.getInstructorRequestId())
                    .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_REQUEST_NOT_FOUND));

            instructorRequestMapper.updateInstructorRequest(instructorRequest, instructorRequestCreationRequest);

            instructorRequestRepository.save(instructorRequest);

            result.setResult(Boolean.TRUE);
        }

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

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ReturnResult<PagedData<InstructorRequestResponse, PageConfig>> GetAllInstructorRequestOfInstructorPaging(PageConfig pageConfig, String ínstructorId) {
        var result  = new ReturnResult<PagedData<InstructorRequestResponse, PageConfig>>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Instructor instructor;
        if(profile.getRole().getRoleName().equals("ADMIN")) {
            instructor = instructorRepository.findByInstructorId(ínstructorId)
                    .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));
        } else {
            instructor = profile.getInstructor();
        }

        if(instructor == null) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND);
        }

        // Clone the original PageConfig to keep the original unchanged
        PageConfig customPageConfig = PageConfig.builder()
                .pageSize(pageConfig.getPageSize())
                .currentPage(pageConfig.getCurrentPage())
                .orders(new ArrayList<>(pageConfig.getOrders()))
                .filters(new ArrayList<>(pageConfig.getFilters()))
                .build();

        List<FilterMapping> filterMappings = customPageConfig.getFilters();
        filterMappings.add(FilterMapping.builder()
                .prop("instructor.instructorId")
                .value(instructor.getInstructorId())
                .type(FilterType.TEXT)
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetInstructorsRequestPaging(customPageConfig).getResult();

        // Set data for page
        PageConfig pageConfigResult = PageConfig
                .builder()
                .pageSize(data.getPageConfig().getPageSize())
                .totalRecords(data.getPageConfig().getTotalRecords())
                .totalPage(data.getPageConfig().getTotalPage())
                .currentPage(data.getPageConfig().getCurrentPage())
                .orders(pageConfig.getOrders())
                .filters(pageConfig.getFilters())
                .build();

        // Build the PagedData object
        result.setResult(
                PagedData.<InstructorRequestResponse, PageConfig>builder()
                        .data(data.getData())
                        .pageConfig(pageConfigResult)
                        .build()
        );

        return result;
    }
}
