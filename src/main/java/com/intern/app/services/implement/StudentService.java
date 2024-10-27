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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService implements IStudentService {
    StudentRepository studentRepository;
    AuthenticationService authenticationService;
    StudentMapper studentMapper;
    FacultyMapper facultyMapper;
    ProfileMapper profileMapper;
    MajorMapper majorMapper;
    UploadContentMapper uploadContentMapper;
    ProfileRepository profileRepository;
    MajorRepository majorRepository;
    FacultyRepository facultyRepository;
    private final InstructorRequestRepository instructorRequestRepository;
    private final InstructorRequestMapper instructorRequestMapper;
    private final InstructorMapper instructorMapper;

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

    // NOT FINISH
    public ReturnResult<Boolean> CreateStudent(StudentCreationRequest studentCreationRequest) {
        ReturnResult<Boolean> result = new ReturnResult<>();

        Boolean isStudentExist = studentRepository.existsById(studentCreationRequest.getStudentId());

        if (isStudentExist) {
            throw new AppException(ErrorCode.STUDENT_EXISTED_ID);
        }

        Student student = studentMapper.toStudent(studentCreationRequest);

        return result;
    }

    public ReturnResult<PagedData<StudentResponse, PageConfig>> GetStudentPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<StudentResponse, PageConfig>>();

        FilterSpecification<Student> filter = new FilterSpecification<Student>();
        Specification<Student> studentFilter = filter.GetSearchSpecification(pageConfig.getFilters());

        Sort sort = pageConfig.getSort();
        Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);

        Page<Student> studentPage = studentRepository.findAll(studentFilter, pageable);

        // Convert Student entities to StudentResponse DTOs
        List<StudentResponse> studentResponses = studentPage.getContent().stream()
                .map(student -> {
                    StudentResponse studentResponse = studentMapper.toStudentResponse(student);
                    studentResponse.setProfile(profileMapper.toProfileResponse(student.getProfile()));
                    return studentResponse;
                })
                .toList();

        // Set data for page
        PageConfig newPageConfig = PageConfig.builder()
                .currentPage(studentPage.getNumber() + 1)
                .pageSize(studentPage.getSize())
                .totalRecords((int) studentPage.getTotalElements())
                .totalPage(studentPage.getTotalPages())
                .orders(pageConfig.getOrders())
                .filters(pageConfig.getFilters())
                .build();

        // Build the PagedData object
        result.setResult(PagedData.<StudentResponse, PageConfig>builder()
                .data(studentResponses)
                .pageConfig(newPageConfig)
                .build());

        return result;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
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

    public ReturnResult<PagedData<InstructorRequestResponse, PageConfig>> GetInstructorsRequestPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<InstructorRequestResponse, PageConfig>>();

        //Get page config filter spec
        FilterSpecification<InstructorRequest> filter = new FilterSpecification<InstructorRequest>();
        Specification<InstructorRequest> instructorRequestSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        // get page config sort
        Sort sort = pageConfig.getSort();
        Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);

        Page<InstructorRequest> instructorRequests = instructorRequestRepository.findAll(instructorRequestSpecification, pageable);

        List<InstructorRequestResponse> instructorRequestResponses = instructorRequests.stream().map(instructorRequest -> {
            InstructorRequestResponse instructorRequestResponse = instructorRequestMapper.toInstructorRequestResponse(instructorRequest);
            instructorRequestResponse.setInstructor(instructorMapper.toInstructorResponse(instructorRequest.getInstructor()));

            return instructorRequestResponse;
        }).toList();

        // Set data for page
        PageConfig pageConfigResult = PageConfig
                .builder()
                .pageSize(instructorRequests.getSize())
                .totalRecords((int) instructorRequests.getTotalElements())
                .totalPage(instructorRequests.getTotalPages())
                .currentPage(instructorRequests.getNumber())
                .orders(pageConfig.getOrders())
                .filters(pageConfig.getFilters())
                .build();

        // Build the PagedData object
        result.setResult(
                PagedData.<InstructorRequestResponse, PageConfig>builder()
                        .data(instructorRequestResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        result.setCode(HttpStatus.OK.value());
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

        var data = this.GetInstructorsRequestPaging(customPageConfig).getResult();

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
}
