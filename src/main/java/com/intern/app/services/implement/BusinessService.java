package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.BusinessMapper;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.mapper.RecruitmentRequestMapper;
import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.request.BusinessUpdateRequest;
import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestGrading;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.repository.*;

import com.intern.app.services.interfaces.IBusinessService;
import com.intern.app.services.interfaces.INotificationService;
import com.intern.app.services.interfaces.IRecruitmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableMethodSecurity
public class BusinessService implements IBusinessService {

    BusinessMapper businessMapper;
    ProfileMapper profileMapper;
    ProfileRepository profileRepository;
    BusinessRepository businessRepository;
    RoleRepository roleRepository;
    ProfileService profileService;

    RecruitmentRequestRepository recruitmentRequestRepository;
    StudentRepository studentRepository;
    IRecruitmentService recruitmentService;
    INotificationService notificationService;
    RecruitmentRequestMapper recruitmentRequestMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> CreateBusiness(BusinessCreationRequest businessCreationRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        var result = new ReturnResult<Boolean>();

        Business business = businessMapper.toBusiness(businessCreationRequest);
        Profile businessManager = profileMapper.toProfile(businessCreationRequest.getManagedBy());

        //Get role
        Role businessRole = roleRepository.findByRoleName("BUSINESS").orElse(null);
        if(businessManager == null) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        //Create profile with role
        Profile savedProfile = profileService.CreateUser(businessCreationRequest.getManagedBy(), businessRole).getResult();
        business.setManagedBy(savedProfile);

        //Create business base on profile
        Business savedBusiness = businessRepository.save(business);
        if(savedBusiness.getBusinessId() == null) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        result.setCode(200);
        result.setResult(true);
        return result;
    }

    @PreAuthorize("hasAuthority('SET_RECRUITMENT_BUSINESS_STATUS')")
    public ReturnResult<Boolean> SetRecruitmentRequestStatus(RequestStatus requestStatus, String recruitmentRequestId) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        RecruitmentRequest recruitmentRequest = recruitmentRequestRepository.findByRecruitmentRequestId(recruitmentRequestId).orElse(null);
        if(recruitmentRequest == null) {
            throw new AppException(ErrorCode.RECRUITMENT_REQUEST_NOT_EXIST);
        }

        Business business = profileRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))
                .getBusiness();

        if(business == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
        }

        if(!recruitmentRequest.getRecruitment().getBusiness().equals(business)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        boolean isApproved = recruitmentRequestRepository.findAllByStudentStudentId(recruitmentRequest.getStudent().getStudentId())
                .stream()
                .anyMatch((req) -> req.getBusinessStatus().equals(RequestStatus.APPROVED));

        if(isApproved) {
            result.setResult(Boolean.FALSE);
            result.setMessage("Sinh viên đã được doanh nghiệp khác chọn, vui lòng tải lại trang");
            result.setCode(200);
        }
        else {
            recruitmentRequest.setBusinessStatus(requestStatus);
            recruitmentRequestRepository.save(recruitmentRequest);

            // SEND NOTIFICATION
            String messageResult = "";
            switch (recruitmentRequest.getBusinessStatus()) {
                case APPROVED:
                    messageResult = "chấp thuận";
                    break;
                case REJECT:
                    messageResult = "từ chối";
                    break;
                case null, default:
                    break;
            }

            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .read(false)
                    .title("Yêu cầu thực tập của bạn đã có kết quả")
                    .content("Yêu cầu đến bài tuyển dụng " + recruitmentRequest.getRecruitment().getTitle() + " đã được " + messageResult)
                    .path("/student/recruitment")
                    .ownerId(recruitmentRequest.getRecruitment().getBusiness().getManagedBy().getProfileId())
                    .profileId(recruitmentRequest.getStudent().getProfile().getProfileId())
                    .build();
            notificationService.SaveNotification(notificationRequest);


            // switch student IsSeekingIntern to false if approve
            if(requestStatus == RequestStatus.APPROVED) {
                Student student = recruitmentRequest.getStudent();
                recruitmentService.ClearAllStudentAvailableRecruitmentRequests(student);
                student.setIsSeekingIntern(false);
                studentRepository.save(student);
            }

            result.setResult(Boolean.TRUE);
            result.setCode(200);
        }

        return result;
    }

    public ReturnResult<BusinessResponse> GetBusinessData(String businessId) {
        var result = new ReturnResult<BusinessResponse>();

        Business business = businessRepository.findById(businessId).orElse(null);
        if(business == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
        }

        Profile profile = business.getManagedBy();
        if(profile == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        ProfileResponse profileResponse = profileMapper.toProfileResponse(profile);
        BusinessResponse businessResponse =  businessMapper.toBusinessResponse(business);

        businessResponse.setManagedBy(profileResponse);

        result.setResult(businessResponse);
        result.setCode(200);

        return result;
    }

    public ReturnResult<BusinessResponse> GetMyBusinessData() {
        var result = new ReturnResult<BusinessResponse>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Business business = profile.getBusiness();
        if(business == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
        }

        BusinessResponse businessResponse =  businessMapper.toBusinessResponse(business);
        businessResponse.setManagedBy(profileMapper.toProfileResponse(profile));

        result.setResult(businessResponse);
        result.setCode(200);

        return result;
    }

    @Override
    public ReturnResult<Boolean> GradePoint(RecruitmentRequestGrading recruitmentRequestGrading) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Business business = profile.getBusiness();
        RecruitmentRequest recruitmentRequest = recruitmentRequestRepository.findByRecruitmentRequestId(recruitmentRequestGrading.getRecruitmentRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.RECRUITMENT_REQUEST_NOT_EXIST));

        if(!Objects.equals(recruitmentRequest.getRecruitment().getBusiness().getBusinessId(), business.getBusinessId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if(recruitmentRequest.getBusinessStatus() == RequestStatus.PENDING ||
                recruitmentRequest.getBusinessStatus() == RequestStatus.REJECT) {
            result.setResult(Boolean.FALSE);
            result.setCode(200);
            result.setMessage("Đơn ứng tuyển chưa được xét duyệt hoặc đã bị từ chối, không thể cho điểm thực tập");
        }

        if(result.getMessage() == null) {
            recruitmentRequestMapper.updateRecruitmentRequestWithGrading(recruitmentRequest, recruitmentRequestGrading);
            recruitmentRequest.setBusinessStatus(RequestStatus.COMPLETED);
            recruitmentRequestRepository.save(recruitmentRequest);

            result.setResult(Boolean.TRUE);
            result.setCode(200);
        }

        return result;
    }

    @PreAuthorize("hasAuthority('UPDATE_BUSINESS_PROFILE')")
    public ReturnResult<Boolean> UpdateBusinessProfile(BusinessUpdateRequest businessUpdateRequest) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElse(null);
        if(profile == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        Business business = profile.getBusiness();
        if(business == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
        }

        profileMapper.updateProfile(profile, businessUpdateRequest.getManagedBy());
        businessMapper.updateBusiness(business, businessUpdateRequest);

        if (!profile.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            throw new AppException(ErrorCode.INVALID_FORMAT_PHONENUMBER);
        }

        profileRepository.save(profile);
        businessRepository.save(business);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ReturnResult<Boolean> UpdateBusinessProfileById(String businessId, BusinessUpdateRequest businessUpdateRequest) {
        var result = new ReturnResult<Boolean>();

        Business business = businessRepository.findById(businessId).orElse(null);
        if(business == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
        }

        Profile profile = business.getManagedBy();
        if(profile == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        profileMapper.updateProfile(profile, businessUpdateRequest.getManagedBy());
        businessMapper.updateBusiness(business, businessUpdateRequest);

        profileRepository.save(profile);
        businessRepository.save(business);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }
}
