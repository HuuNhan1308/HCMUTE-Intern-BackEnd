package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.*;
import com.intern.app.models.dto.datamodel.*;
import com.intern.app.models.dto.request.StudentCreationRequest;
import com.intern.app.models.dto.request.StudentUpdateRequest;
import com.intern.app.models.dto.response.*;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.FilterOperator;
import com.intern.app.models.enums.FilterType;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.repository.*;

import com.intern.app.services.interfaces.IPagingService;
import com.intern.app.services.interfaces.IStudentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.RequestToViewNameTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService implements IStudentService {
    StudentRepository studentRepository;
    StudentMapper studentMapper;
    ProfileMapper profileMapper;
    MajorMapper majorMapper;
    ProfileRepository profileRepository;
    IPagingService pagingService;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    MajorRepository majorRepository;
    InstructorRequestRepository instructorRequestRepository;
    RecruitmentRequestRepository recruitmentRequestRepository;

    public ReturnResult<StudentResponse> FindStudentById(String id) {
        var result = new ReturnResult<StudentResponse>();
        Student student = studentRepository.findById(id).orElse(null);

        if (student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        } else {
            StudentResponse studentResponse = studentMapper.toStudentResponse(student);
            studentResponse.setMajor(majorMapper.toMajorResponse(student.getMajor()));
            studentResponse.setProfile(profileMapper.toProfileResponse(student.getProfile()));

            result.setResult(studentResponse);
            result.setCode(HttpStatus.OK.value());
        }

        return result;
    }

    public ReturnResult<StudentResponse> GetStudentByUsername(String username) {
        var result = new ReturnResult<StudentResponse>();

        Profile profile = profileRepository.findByUsername(username).orElse(null);
        if (profile.getDeleted()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        Student student = studentRepository.findByProfile(profile).orElse(null);
        if (student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        } else {
            StudentResponse studentResponse = studentMapper.toStudentResponse(student);
            studentResponse.setMajor(majorMapper.toMajorResponse(student.getMajor()));
            studentResponse.setProfile(profileMapper.toProfileResponse(student.getProfile()));

            result.setResult(studentResponse);
            result.setCode(HttpStatus.OK.value());
        }

        return result;
    }

    @PreAuthorize("hasAuthority('CREATE_STUDENT')")
    public ReturnResult<Boolean> CreateStudent(StudentCreationRequest studentCreationRequest) {
        ReturnResult<Boolean> result = new ReturnResult<>();

        boolean isStudentExist = studentRepository.existsById(studentCreationRequest.getStudentId());
        Role studentRole = roleRepository.findByRoleName("STUDENT").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

        if (isStudentExist) {
            throw new AppException(ErrorCode.STUDENT_EXISTED_ID);
        }

        Major major = majorRepository.findByMajorId(studentCreationRequest.getMajorId())
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_EXISTED));

        Student student = studentMapper.toStudent(studentCreationRequest);
        student.setMajor(major);

        Profile profile = profileMapper.toProfile(studentCreationRequest.getProfile());
        profile.setRole(studentRole);
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        profile = profileRepository.save(profile);

        student.setProfile(profile);
        studentRepository.save(student);

        result.setResult(Boolean.TRUE);
        result.setCode(HttpStatus.OK.value());

        return result;
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ReturnResult<Boolean> UpdateStudent(StudentUpdateRequest studentUpdateRequest) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Student student = profile.getStudent();

        if (student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        profileMapper.updateProfile(profile, studentUpdateRequest.getProfile());
        studentMapper.updateStudent(student, studentUpdateRequest);

        if (!profile.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            throw new AppException(ErrorCode.INVALID_FORMAT_PHONENUMBER);
        }

        profileRepository.save(profile);
        studentRepository.save(student);

        return result;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public ReturnResult<Boolean> DeleteInstructorRequests(List<String> instructorRequestIds) {
        var result = new ReturnResult<Boolean>();

        // Get the authenticated student's information
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findById(username)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        // Fetch the instructor requests by IDs
        List<InstructorRequest> instructorRequests = instructorRequestRepository.findByInstructorRequestIdIn(instructorRequestIds);

        // Validate the requests
        for (InstructorRequest instructorRequest : instructorRequests) {
            // Ensure the request belongs to the current student
            if (!Objects.equals(instructorRequest.getStudent().getStudentId(), student.getStudentId())) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            // Ensure the request is in a PENDING state
            if (instructorRequest.getInstructorStatus() != RequestStatus.PENDING) {
                throw new AppException(ErrorCode.NOT_PENDING_REQUEST);
            }
        }

        // Perform soft delete for all valid requests
        instructorRequestRepository.softDeleteRange(instructorRequests);

        result.setResult(Boolean.TRUE);
        result.setCode(200);
        return result;
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public ReturnResult<Boolean> DeleteRecruitmentRequests(List<String> recruitmentRequestIds) {
        var result = new ReturnResult<Boolean>();

        // Get the authenticated student's information
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findById(username)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        // Fetch the recruitment requests by IDs
        List<RecruitmentRequest> recruitmentRequests = recruitmentRequestRepository.findByRecruitmentRequestIdIn(recruitmentRequestIds);

        // Validate the requests
        for (RecruitmentRequest recruitmentRequest : recruitmentRequests) {
            // Ensure the request belongs to the current student
            if (!Objects.equals(recruitmentRequest.getStudent().getStudentId(), student.getStudentId())) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            // Ensure the request is in a PENDING state
            if (recruitmentRequest.getBusinessStatus() != RequestStatus.PENDING) {
                throw new AppException(ErrorCode.NOT_PENDING_REQUEST);
            }
        }

        // Perform soft delete for all valid requests
        recruitmentRequestRepository.softDeleteRange(recruitmentRequests);

        result.setResult(Boolean.TRUE);
        result.setCode(200);
        return result;
    }


    @PreAuthorize("hasAnyRole('STUDENT')")
    public ReturnResult<PagedData<InstructorRequestResponse, PageConfig>> GetAllStudentInstructorsRequestPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<InstructorRequestResponse, PageConfig>>();
        var context = SecurityContextHolder.getContext();
        var username = context.getAuthentication().getName();
        Student student = profileRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))
                .getStudent();

        // Clone the original PageConfig to keep the original unchanged
        PageConfig customPageConfig = PageConfig.builder()
                .pageSize(pageConfig.getPageSize())
                .currentPage(pageConfig.getCurrentPage())
                .orders(new ArrayList<>(pageConfig.getOrders()))
                .filters(new ArrayList<>(pageConfig.getFilters()))
                .build();

        List<FilterMapping> filterMappings = customPageConfig.getFilters();
        filterMappings.add(FilterMapping.builder()
                .prop("student.studentId")
                .value(student.getStudentId())
                .type(FilterType.TEXT)
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetInstructorsRequestPaging(customPageConfig).getResult();

        data.setData(data.getData().stream().peek(e -> e.setStudent(null)).toList());

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

        result.setCode(HttpStatus.OK.value());

        return result;
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> GetAllStudentRecruitmentsRequestPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>>();

        var context = SecurityContextHolder.getContext();
        var username = context.getAuthentication().getName();
        Student student = profileRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))
                .getStudent();

        // Clone the original PageConfig to keep the original unchanged
        PageConfig customPageConfig = PageConfig.builder()
                .pageSize(pageConfig.getPageSize())
                .currentPage(pageConfig.getCurrentPage())
                .orders(new ArrayList<>(pageConfig.getOrders()))
                .filters(new ArrayList<>(pageConfig.getFilters()))
                .build();

        List<FilterMapping> filterMappings = customPageConfig.getFilters();
        filterMappings.add(FilterMapping.builder()
                .prop("student.studentId")
                .value(student.getStudentId())
                .type(FilterType.TEXT)
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetRecruitmentRequestPaging(customPageConfig).getResult();

        data.setData(data.getData().stream().peek(e -> e.setStudent(null)).toList());

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
                PagedData.<RecruitmentRequestResponse, PageConfig>builder()
                        .data(data.getData())
                        .pageConfig(pageConfigResult)
                        .build()
        );
        result.setCode(HttpStatus.OK.value());

        return result;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS')")
    public ReturnResult<PagedData<StudentResponse, PageConfig>> GetAllStudentWithSeekingIntern(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<StudentResponse, PageConfig>>();


        // Clone the original PageConfig to keep the original unchanged
        PageConfig customPageConfig = PageConfig.builder()
                .pageSize(pageConfig.getPageSize())
                .currentPage(pageConfig.getCurrentPage())
                .orders(new ArrayList<>(pageConfig.getOrders()))
                .filters(new ArrayList<>(pageConfig.getFilters()))
                .build();

        List<FilterMapping> filterMappings = customPageConfig.getFilters();
        filterMappings.add(FilterMapping.builder()
                .prop("isSeekingIntern")
                .value("TRUE")
                .type(FilterType.BOOLEAN)
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetStudentPaging(customPageConfig).getResult();

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
                PagedData.<StudentResponse, PageConfig>builder()
                        .data(data.getData())
                        .pageConfig(pageConfigResult)
                        .build()
        );
        result.setCode(HttpStatus.OK.value());

        return result;
    }
}
