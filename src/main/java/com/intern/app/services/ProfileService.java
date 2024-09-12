package com.intern.app.services;

import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Profile;
import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.repository.ProfileRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    ProfileMapper profileMapper;

    public ReturnResult<Boolean> CreateUser(ProfileCreationRequest request) {
        ReturnResult<Boolean> result = new ReturnResult<Boolean>();

        //check exist
        boolean isExistProfile = profileRepository.findByUsername(request.getUsername()).isPresent();

        if(isExistProfile) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        Profile savedProfile = profileMapper.toProfile(request);
        savedProfile.setPassword(passwordEncoder.encode(request.getPassword()));
        savedProfile = profileRepository.save(savedProfile);

        result.setResult(savedProfile.getProfileId() != null);
        result.setCode(200);

        return result;
    }

    public ReturnResult<ProfileResponse> FindProfileById(String profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return ReturnResult.<ProfileResponse>builder().result(profileMapper.toProfileResponse(profile)).build();
    }


}
