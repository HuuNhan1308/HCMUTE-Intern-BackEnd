package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.BusinessMapper;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.request.BusinessUpdateRequest;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.repository.*;

import com.intern.app.services.interfaces.IBusinessService;
import com.intern.app.services.interfaces.IRecruitmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


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

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> CreateBusiness(BusinessCreationRequest businessCreationRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
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

    //Cần phải test các case RecruitmentRequestStatus khác nhau khi gửi từ postman
    @PreAuthorize("hasAuthority('SET_RECRUITMENT_BUSINESS_STATUS')")
    public ReturnResult<Boolean> SetRecruitmentRequestStatus(RequestStatus requestStatus, String recruitmentRequestId) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        RecruitmentRequest recruitmentRequest = recruitmentRequestRepository.findByRecruitmentRequestId(recruitmentRequestId).orElse(null);
        if(recruitmentRequest == null) {
            throw new AppException(ErrorCode.RECRUITMENT_REQUEST_NOT_EXIST);
        }

        Profile profile = profileRepository.findByUsernameAndDeletedFalse(username).orElse(null);
        if(profile == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        Business business = profile.getBusiness();
        if(business == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
        }

        if(!recruitmentRequest.getRecruitment().getBusiness().equals(business)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        recruitmentRequest.setBusinessStatus(requestStatus);
        recruitmentRequestRepository.save(recruitmentRequest);

        // switch student IsSeekingIntern to false if approve
        if(requestStatus == RequestStatus.APPROVED) {
            Student student = recruitmentRequest.getStudent();
            recruitmentService.ClearAllStudentAvailableRecruitmentRequests(student);
            student.setIsSeekingIntern(false);
        studentRepository.save(student);
        }

        result.setResult(Boolean.TRUE);
        result.setCode(200);

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
