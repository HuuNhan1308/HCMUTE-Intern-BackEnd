package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.BusinessMapper;
import com.intern.app.mapper.RecruitmentMapper;
import com.intern.app.mapper.RecruitmentRequestMapper;
import com.intern.app.models.dto.datamodel.FilterSpecification;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.response.RecruitmentResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;
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

    public ReturnResult<PagedData<RecruitmentResponse, PageConfig>> GetRecruitmentPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<RecruitmentResponse, PageConfig>>();

        FilterSpecification<Recruitment> filter = new FilterSpecification<>();
        Specification<Recruitment> recruitmentFilter = filter.GetSearchSpecification(pageConfig.getFilters());

        Sort sort = pageConfig.getSort();
        Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);

        Page<Recruitment> recruitmentPage = recruitmentRepository.findAll(recruitmentFilter, pageable);

        //Convert result to response
        List<RecruitmentResponse> recruitmentResponses = recruitmentPage.getContent().stream()
                .map(recruitment -> {
                    RecruitmentResponse recruitmentResponse = recruitmentMapper.toRecruitmentResponse(recruitment);
                    recruitmentResponse.setBusiness(businessMapper.toBusinessResponse(recruitment.getBusiness()));
                    return recruitmentResponse;
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
                PagedData.<RecruitmentResponse, PageConfig>builder()
                        .data(recruitmentResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        result.setCode(200);

        return result;
    }
}