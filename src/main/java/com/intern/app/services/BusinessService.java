package com.intern.app.services;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.BusinessMapper;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Business;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Role;
import com.intern.app.repository.BusinessRepository;
import com.intern.app.repository.ProfileRepository;

import com.intern.app.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableMethodSecurity
public class BusinessService {

    BusinessMapper businessMapper;
    ProfileMapper profileMapper;
    ProfileRepository profileRepository;
    BusinessRepository businessRepository;
    RoleRepository roleRepository;
    ProfileService profileService;

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
        if(savedBusiness == null) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        result.setCode(200);
        result.setResult(true);
        return result;
    }
}
