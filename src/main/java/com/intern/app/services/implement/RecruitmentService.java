package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.BusinessMapper;
import com.intern.app.mapper.NotificationMapper;
import com.intern.app.mapper.RecruitmentMapper;
import com.intern.app.mapper.RecruitmentRequestMapper;
import com.intern.app.models.dto.datamodel.FilterMapping;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.request.RecruitmentUpdateRequest;
import com.intern.app.models.dto.response.*;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.FilterOperator;
import com.intern.app.models.enums.FilterType;
import com.intern.app.models.enums.RecruitmentStatus;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.repository.*;
import com.intern.app.services.interfaces.IAuthenticationService;
import com.intern.app.services.interfaces.INotificationService;
import com.intern.app.services.interfaces.IRecruitmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.Request;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    RecruitmentRequestRepository recruitmentRequestRepository;
    BusinessMapper businessMapper;
    StudentRepository studentRepository;
    PagingService pagingService;
    BusinessRepository businessRepository;
    INotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @PreAuthorize("hasRole('BUSINESS')")
    public ReturnResult<Boolean> CreateRecruitment(RecruitmentCreationRequest recruitmentCreationRequest) {
        var result = new ReturnResult<Boolean>();

        Recruitment recruitment = recruitmentMapper.toRecruitment(recruitmentCreationRequest);

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Optional<Profile> profile = profileRepository.findByUsername(username);

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

    @PreAuthorize("hasAnyRole('BUSINESS', 'ADMIN')")
    public ReturnResult<Boolean> UpdateRecruitment(RecruitmentUpdateRequest recruitmentUpdateRequest, String businessId) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Recruitment recruitment = recruitmentRepository.findByRecruitmentId(recruitmentUpdateRequest.getRecruitmentId())
                .orElseThrow(() -> new AppException(ErrorCode.RECRUITMENT_NOT_FOUND));
        Profile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Business business;
        if(profile.getRole().getRoleName().equals("ADMIN")) {
            business = businessRepository.findById(businessId)
                    .orElseThrow(() -> new AppException(ErrorCode.BUSINESS_NOT_FOUND));
        }
        else {
            business = profile.getBusiness();
        }

        if(recruitment.getBusiness() != business) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        recruitmentMapper.updateRecruitment(recruitment, recruitmentUpdateRequest);
        recruitmentRepository.save(recruitment);

        if(recruitment.getStatus() == RecruitmentStatus.CLOSED)
            this.RejectAllRecruitmentRequest(recruitment);

        result.setCode(200);
        result.setResult(true);

        return result;
    }

    public ReturnResult<Boolean> RejectAllRecruitmentRequest(Recruitment recruitment) {
        var result = new ReturnResult<Boolean>();

        List<RecruitmentRequest> recruitmentRequests = recruitmentRequestRepository.findByRecruitmentAndBusinessStatus(recruitment, RequestStatus.PENDING);

        recruitmentRequests.stream()
                .peek(recruitmentRequest -> recruitmentRequest.setBusinessStatus(RequestStatus.REJECT))
                .toList();

        recruitmentRequestRepository.saveAll(recruitmentRequests);

        result.setCode(200);
        result.setResult(Boolean.TRUE);

        return result;
    }

    @PreAuthorize("hasAuthority('REQUEST_RECRUITMENT')")
    public ReturnResult<Boolean> RequestRecruitment(RecruitmentRequestCreationRequest recruitmentRequestCreationRequest) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        Student student = studentRepository.findById(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Recruitment recruitment = recruitmentRepository.findByRecruitmentId(recruitmentRequestCreationRequest.getRecruitmentId())
                .orElseThrow(() -> new AppException(ErrorCode.RECRUITMENT_NOT_FOUND));

        //CHECK IF THE RECRUITMENT STATUS IS CLOSE SO THAT STUDENT CANT SEND REQUEST
        if(recruitment.getStatus() == RecruitmentStatus.CLOSED) {
            result.setMessage("Bài tuyển dụng này đã bị đóng, không gửi yêu cầu hoặc chỉnh sửa thêm được nữa");
        }

        if(recruitmentRequestCreationRequest.getRecruitmentRequestId() == null) {
            //CASE ADD
            //CHECK IF STUDENT ALREADY HAVE APPROVED BY ANY RECRUITMENT?
            RecruitmentRequest approvedRecruitmentRequest = recruitmentRequestRepository
                    .findByStudentStudentIdAndBusinessStatus(student.getStudentId(), RequestStatus.APPROVED)
                    .orElse(null);

            if(approvedRecruitmentRequest != null) {
                result.setMessage("Bạn đã được chọn bởi một doanh nghiệp khác, không thể gửi thêm yêu cầu thực tập được nữa...");
                result.setResult(Boolean.FALSE);
            }

            //CHECK IF STUDENT SEND TO THE SAME RECRUITMENT POST
            RecruitmentRequest recruitmentRequest = recruitmentRequestRepository
                    .findByStudentStudentIdAndRecruitmentRecruitmentIdAndBusinessStatus(
                            student.getStudentId(),
                            recruitment.getRecruitmentId(),
                            RequestStatus.PENDING
                    ).orElse(null);

            if(recruitmentRequest != null) {
                result.setResult(Boolean.FALSE);
                result.setMessage("Bạn đã gửi yêu cầu cho doanh nghiệp này rồi, vui lòng chờ đợi phản hồi");
            }

            if(result.getMessage() == null) {
                recruitmentRequest = RecruitmentRequest.builder()
                        .student(student)
                        .recruitment(recruitment)
                        .businessStatus(RequestStatus.PENDING)
                        .messageToBusiness(recruitmentRequestCreationRequest.getMessageToBusiness())
                        .build();

                recruitmentRequestRepository.save(recruitmentRequest);

                // SEND NOTIFICATION
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .read(false)
                        .title("Bạn vừa nhận được một yêu cầu thực tập mới")
                        .content("Yêu cầu thực tập đến từ bài tuyển dụng: " + recruitment.getTitle())
                        .ownerId(student.getProfile().getProfileId())
                        .profileId(recruitment.getBusiness().getManagedBy().getProfileId())
                        .build();
                notificationService.SaveNotification(notificationRequest);

                result.setResult(Boolean.TRUE);
            }

        } else {
            // CASE EDIT
            RecruitmentRequest recruitmentRequest = recruitmentRequestRepository
                    .findByRecruitmentRequestId(recruitmentRequestCreationRequest.getRecruitmentRequestId())
                    .orElseThrow(() -> new AppException(ErrorCode.RECRUITMENT_REQUEST_NOT_EXIST));

            if(!Objects.equals(recruitmentRequest.getStudent(), student))
                throw new AppException(ErrorCode.UNAUTHORIZED);

            if(result.getMessage() == null) {
                recruitmentRequestMapper.updateRecruitmentRequest(recruitmentRequest, recruitmentRequestCreationRequest);
                recruitmentRequestRepository.save(recruitmentRequest);
                result.setResult(Boolean.TRUE);
            }

        }

        result.setCode(200);

        return result;
    }

    public ReturnResult<Boolean> ClearAllStudentAvailableRecruitmentRequests(Student student) {
        var result = new ReturnResult<Boolean>();

        List<RecruitmentRequest> recruitmentRequests = recruitmentRequestRepository.findByStudentAndStatus(student, RequestStatus.PENDING);

        recruitmentRequests = recruitmentRequests.stream()
                .peek(recruitmentRequest -> recruitmentRequest.setBusinessStatus(RequestStatus.REJECT))
                .toList();

        recruitmentRequestRepository.saveAll(recruitmentRequests);

        result.setCode(200);
        result.setResult(true);

        return result;
    }



    public ReturnResult<RecruitmentResponse> GetRecruitmentById(String recruitmentId){
        var result = new ReturnResult<RecruitmentResponse>();

        Recruitment recruitment = recruitmentRepository.findByRecruitmentId(recruitmentId).orElse(null);
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

    @PreAuthorize("hasAnyRole('BUSINESS', 'ADMIN')")
    public ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetAllBusinessRecruitmentPaging(PageConfig pageConfig, String businessId) {
        var result = new ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Business business;
        if(profile.getRole().getRoleName().equals("ADMIN")) {
            business = businessRepository.findById(businessId)
                    .orElseThrow(() -> new AppException(ErrorCode.BUSINESS_NOT_FOUND));
        }
        else {
            business = profile.getBusiness();
        }

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
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetRecruitmentPaging(customPageConfig).getResult();

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

    public ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetOpenRecruitmentPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>>();

        // Clone the original PageConfig to keep the original unchanged
        PageConfig customPageConfig = PageConfig.builder()
                .pageSize(pageConfig.getPageSize())
                .currentPage(pageConfig.getCurrentPage())
                .orders(new ArrayList<>(pageConfig.getOrders()))
                .filters(new ArrayList<>(pageConfig.getFilters()))
                .build();

        List<FilterMapping> filterMappings = customPageConfig.getFilters();
        filterMappings.add(FilterMapping.builder()
                .prop("status")
                .value(String.valueOf(RecruitmentStatus.OPEN.ordinal()))
                .type(FilterType.NUMBER)
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetRecruitmentPaging(customPageConfig).getResult();

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

    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS')")
    public ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> GetAllRecruitmentRequestOfRecruitmentPaging(PageConfig pageConfig, String recruitmentId) {
        var result = new ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new AppException(ErrorCode.RECRUITMENT_NOT_FOUND));

        if(profile.getRole().getRoleName().equals("BUSINESS") && recruitment.getBusiness() != profile.getBusiness()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
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
                .prop("recruitment.recruitmentId")
                .value(recruitment.getRecruitmentId())
                .type(FilterType.TEXT)
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetRecruitmentRequestPaging(customPageConfig).getResult();
        // Clean up data of recruitment
        data.setData(data.getData().stream().peek(x -> x.setRecruitment(null)).toList());

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


        return result;
    }


    @PreAuthorize("hasAnyRole('BUSINESS')")
    public ReturnResult<Boolean> InviteStudent(String recruitmentId, String studentId) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Business business = profile.getBusiness();

        Recruitment recruitment = recruitmentRepository.findByRecruitmentId(recruitmentId)
                .orElseThrow(() -> new AppException(ErrorCode.RECRUITMENT_NOT_FOUND));

        if(recruitment.getBusiness() != business)
            throw new AppException(ErrorCode.UNAUTHORIZED);


        NotificationRequest notificationRequest = NotificationRequest.builder()
                .path("/recruitment/" + recruitment.getRecruitmentId())
                .title("Bạn nhận được lời mời tuyển dụng đến từ công ty " + business.getName())
                .content("Lời mời đến từ bài tuyển dụng: " + recruitment.getTitle() + "\nNhấn vào đây để xem chi tiết")
                .ownerId(profile.getProfileId())
                .profileId(studentId)
                .build();

        result = notificationService.SaveNotification(notificationRequest);

        return result;
    }
}

