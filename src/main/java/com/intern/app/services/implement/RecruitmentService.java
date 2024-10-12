package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.BusinessMapper;
import com.intern.app.mapper.RecruitmentMapper;
import com.intern.app.mapper.RecruitmentRequestMapper;
import com.intern.app.models.dto.datamodel.FilterMapping;
import com.intern.app.models.dto.datamodel.FilterSpecification;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.RecruitmentResponse;
import com.intern.app.models.dto.response.RecruitmentResponseShort;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.FilterOperator;
import com.intern.app.models.enums.FilterType;
import com.intern.app.models.enums.RecruitmentRequestStatus;
import com.intern.app.repository.*;
import com.intern.app.services.interfaces.IRecruitmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableMethodSecurity
public class RecruitmentService implements IRecruitmentService {
    RecruitmentMapper recruitmentMapper;
    RecruitmentRequestMapper recruitmentRequestMapper;

    ProfileRepository profileRepository;
    RecruitmentRepository recruitmentRepository;
    InstructorRepository instructorRepository;
    RecruitmentRequestRepository recruitmentRequestRepository;
    private final BusinessMapper businessMapper;

    @PreAuthorize("hasRole('BUSINESS')")
    public ReturnResult<Boolean> CreateRecruitment(RecruitmentCreationRequest recruitmentCreationRequest) {
        var result = new ReturnResult<Boolean>();

        Recruitment recruitment = recruitmentMapper.toRecruitment(recruitmentCreationRequest);

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Optional<Profile> profile = profileRepository.findByUsernameAndDeletedFalse(username);

        if(profile.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        if(profile.get().getBusiness() == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
        }

        recruitment.setBusiness(profile.get().getBusiness());

        Recruitment createdRecruitment = recruitmentRepository.save(recruitment);

        result.setCode(200);
        result.setResult(createdRecruitment != null);

        return result;
    }

    @PreAuthorize("hasAuthority('REQUEST_RECRUITMENT')")
    public ReturnResult<Boolean> RequestRecruitment(RecruitmentRequestCreationRequest recruitmentRequestCreationRequest) {
        var result = new ReturnResult<Boolean>();

        RecruitmentRequest recruitmentRequest = recruitmentRequestMapper.toRecruitmentRequest(recruitmentRequestCreationRequest);

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsernameAndDeletedFalse(username).orElse(null);
        Recruitment recruitment = recruitmentRepository.findByRecruitmentIdAndDeletedFalse(recruitmentRequestCreationRequest.getRecruitmentId()).orElse(null);
        Instructor instructor = instructorRepository.findByInstructorIdAndDeletedFalse(recruitmentRequestCreationRequest.getInstructorId()).orElse(null);

        if(profile == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        if(profile.getStudent() == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }
        recruitmentRequest.setStudent(profile.getStudent());

        if(recruitment == null) {
            throw new AppException(ErrorCode.RECRUITMENT_NOT_FOUND);
        } else {
            recruitmentRequest.setRecruitment(recruitment);
            recruitmentRequest.setBusinessStatus(RecruitmentRequestStatus.PENDING);
        }

        if(instructor != null) {
            recruitmentRequest.setRefInstructor(instructor);
            recruitmentRequest.setInstructorStatus(RecruitmentRequestStatus.PENDING);
        }

        RecruitmentRequest saved = recruitmentRequestRepository.save(recruitmentRequest);

        result.setResult(saved.getRecruitmentRequestId() != null);
        result.setCode(200);

        return result;
    }

    public ReturnResult<Boolean> ClearAllStudentAvailableRecruitmentRequests(Student student) {
        var result = new ReturnResult<Boolean>();

        List<RecruitmentRequest> recruitmentRequests = recruitmentRequestRepository.findByStudentAndStatus(student, RecruitmentRequestStatus.PENDING);

        recruitmentRequestRepository.softDeleteRange(recruitmentRequests);

        result.setCode(200);
        result.setResult(true);

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

    public ReturnResult<RecruitmentResponse> GetRecruitmentById(String recruitmentId){
        var result = new ReturnResult<RecruitmentResponse>();

        Recruitment recruitment = recruitmentRepository.findByRecruitmentIdAndDeletedFalse(recruitmentId).orElse(null);
        if(recruitment == null) {
            throw new AppException(ErrorCode.RECRUITMENT_NOT_FOUND);
        }

        RecruitmentResponse recruitmentResponse = recruitmentMapper.toRecruitmentResponse(recruitment);
        BusinessResponse businessResponse = businessMapper.toBusinessResponse(recruitment.getBusiness());
        recruitmentResponse.setBusiness(businessResponse);

        result.setResult(recruitmentResponse);
        result.setCode(200);

        return result;
    }

    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetAllBusinessRecruitmentPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Business business = profile.getBusiness();
        if(business == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
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
                .prop("business.businessId")
                .value(business.getBusinessId())
                .type(FilterType.TEXT)
                .operator(FilterOperator.CONTAINS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = this.GetRecruitmentPaging(customPageConfig).getResult();

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
                PagedData.<RecruitmentResponseShort, PageConfig>builder()
                        .data(data.getData())
                        .pageConfig(pageConfigResult)
                        .build()
        );

        result.setCode(200);

        return result;

    }
}
