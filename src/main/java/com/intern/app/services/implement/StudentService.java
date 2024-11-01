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
import com.intern.app.repository.*;

import com.intern.app.services.interfaces.IPagingService;
import com.intern.app.services.interfaces.IStudentService;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.RequestToViewNameTranslator;

import java.util.ArrayList;
import java.util.List;

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
    private final RequestToViewNameTranslator viewNameTranslator;

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

    @Override
    public ReturnResult<Boolean> CreateStudent(StudentCreationRequest studentCreationRequest) {
        return null;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    public ReturnResult<Boolean> UpdateStudent(StudentUpdateRequest studentUpdateRequest) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElse(null);
        Student student = profile.getStudent();

        if (profile == null || student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        studentMapper.updateStudent(student, studentUpdateRequest);
        profileMapper.updateProfile(profile, studentUpdateRequest.getProfile());

        studentRepository.save(student);
        profileRepository.save(profile);

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
}
