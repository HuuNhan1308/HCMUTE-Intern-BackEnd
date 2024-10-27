package com.intern.app.services.implement;

import com.intern.app.mapper.*;
import com.intern.app.models.dto.datamodel.FilterSpecification;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.response.*;
import com.intern.app.models.entity.InstructorRequest;
import com.intern.app.models.entity.Recruitment;
import com.intern.app.models.entity.RecruitmentRequest;
import com.intern.app.models.entity.Student;
import com.intern.app.repository.InstructorRequestRepository;
import com.intern.app.repository.RecruitmentRepository;
import com.intern.app.repository.RecruitmentRequestRepository;
import com.intern.app.repository.StudentRepository;
import com.intern.app.services.interfaces.IPagingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PagingService implements IPagingService {

    InstructorRequestRepository instructorRequestRepository;
    StudentRepository studentRepository;
    RecruitmentRepository recruitmentRepository;
    RecruitmentRequestRepository recruitmentRequestRepository;

    InstructorRequestMapper instructorRequestMapper;
    StudentMapper studentMapper;
    ProfileMapper profileMapper;
    InstructorMapper instructorMapper;
    RecruitmentMapper recruitmentMapper;
    RecruitmentRequestMapper recruitmentRequestMapper;

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

    public ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetRecruitmentPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>>();

        FilterSpecification<Recruitment> filter = new FilterSpecification<>();
        Specification<Recruitment> recruitmentFilter = filter.GetSearchSpecification(pageConfig.getFilters());

        Sort sort = pageConfig.getSortAndNewItem();
        Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);

        Page<Recruitment> recruitmentPage = recruitmentRepository.findAll(recruitmentFilter, pageable);

        //Convert result to response
        List<RecruitmentResponseShort> recruitmentResponsesShort = recruitmentPage.getContent().stream()
                .map(recruitment -> {
                    RecruitmentResponseShort recruitmentResponseShort = recruitmentMapper.toRecruitmentResponseShort(recruitment);
                    recruitmentResponseShort.setBusinessName(recruitment.getBusiness().getName());
                    return recruitmentResponseShort;
                }).toList();

        // Set data for page
        PageConfig pageConfigResult = PageConfig
                .builder()
                .pageSize(recruitmentPage.getSize())
                .totalRecords((int) recruitmentPage.getTotalElements())
                .totalPage(recruitmentPage.getTotalPages())
                .currentPage(recruitmentPage.getNumber())
                .orders(pageConfig.getOrders())
                .filters(pageConfig.getFilters())
                .build();

        // Build the PagedData object
        result.setResult(
                PagedData.<RecruitmentResponseShort, PageConfig>builder()
                        .data(recruitmentResponsesShort)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        result.setCode(200);

        return result;
    }

    public ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> GetRecruitmentRequestPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>>();

        //Specification
        FilterSpecification<RecruitmentRequest> filter = new FilterSpecification<>();
        Specification<RecruitmentRequest> recruitmentRequestSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Sort
        Sort sort = pageConfig.getSortAndNewItem();
        Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);

        //Get data from spec and sort
        Page<RecruitmentRequest> recruitmentRequests = recruitmentRequestRepository.findAll(recruitmentRequestSpecification, pageable);

        //Convert data to response object
        List<RecruitmentRequestResponse> recruitmentRequestResponses = recruitmentRequests.getContent().stream()
                .map(recruitmentRequest -> {
                    RecruitmentRequestResponse recruitmentRequestResponse = recruitmentRequestMapper.toRecruitmentRequestResponse(recruitmentRequest);
                    StudentResponse studentResponse = studentMapper.toStudentResponse(recruitmentRequest.getStudent());
                    RecruitmentResponse recruitmentResponse = recruitmentMapper.toRecruitmentResponse(recruitmentRequest.getRecruitment());

                    recruitmentRequestResponse.setStudent(studentResponse);
                    recruitmentRequestResponse.setRecruitment(recruitmentResponse);

                    return recruitmentRequestResponse;
                }).toList();

        // Set data for page
        PageConfig pageConfigResult = PageConfig
                .builder()
                .pageSize(recruitmentRequests.getSize())
                .totalRecords((int) recruitmentRequests.getTotalElements())
                .totalPage(recruitmentRequests.getTotalPages())
                .currentPage(recruitmentRequests.getNumber())
                .orders(pageConfig.getOrders())
                .filters(pageConfig.getFilters())
                .build();

        // Build the PagedData object
        result.setResult(
                PagedData.<RecruitmentRequestResponse, PageConfig>builder()
                        .data(recruitmentRequestResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );
        return result;
    }
}
