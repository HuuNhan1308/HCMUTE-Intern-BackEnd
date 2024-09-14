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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProfileService {
    ProfileRepository profileRepository;
    AuthenticationService authenticationService;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;

    public ReturnResult<Boolean> CreateUser(ProfileCreationRequest request) {
        ReturnResult<Boolean> result = new ReturnResult<Boolean>();

        //check exist
        boolean isExistProfile = profileRepository.findByUsername(request.getUsername()).isPresent();

        if(isExistProfile) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

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

    public ReturnResult<Boolean> ChangePassword(String oldPassword, String newPassword, String accessToken) throws ParseException {
        var result = new ReturnResult<Boolean>();
        var data = authenticationService.verityToken(accessToken).getJWTClaimsSet();

        String username = data.getSubject();
        Profile profile = profileRepository.findByUsernameAndDeletedFalse(username).orElse(null);

        if(profile == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if(!passwordEncoder.matches(oldPassword, profile.getPassword())) {
            result.setResult(false);
            result.setMessage("Wrong old password...");
            result.setCode(HttpStatus.BAD_REQUEST.value());
        }else {
            profile.setPassword(passwordEncoder.encode(newPassword));
            profileRepository.save(profile);
            result.setResult(true);
            result.setCode(HttpStatus.OK.value());
        }

        return result;
    }
}
