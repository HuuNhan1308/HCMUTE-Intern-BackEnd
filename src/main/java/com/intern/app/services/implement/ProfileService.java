package com.intern.app.services.implement;

import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Profile;
import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.models.entity.Role;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.services.interfaces.IProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProfileService implements IProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;

    public ReturnResult<Profile> CreateUser(ProfileCreationRequest request, Role role) {
        var result = new ReturnResult<Profile>();

        //check exist
        boolean isExistProfile = profileRepository.findByUsername(request.getUsername()).isPresent();

        if(isExistProfile) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Profile savedProfile = profileMapper.toProfile(request);
        savedProfile.setPassword(passwordEncoder.encode(request.getPassword()));
        savedProfile.setRole(role);

        savedProfile = profileRepository.save(savedProfile);

        result.setResult(savedProfile);
        result.setCode(200);

        return result;
    }

    public ReturnResult<ProfileResponse> FindProfileById(String profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return ReturnResult.<ProfileResponse>builder().result(profileMapper.toProfileResponse(profile)).build();
    }

    public ReturnResult<Boolean> ChangePassword(String oldPassword, String newPassword, String username) {
        var result = new ReturnResult<Boolean>();
        Profile profile = profileRepository.findByUsernameAndDeletedFalse(username).orElse(null);

        if(profile == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (!passwordEncoder.matches(oldPassword, profile.getPassword())) {
            result.setResult(false);
            result.setMessage("Wrong old password...");
            result.setCode(HttpStatus.BAD_REQUEST.value());
        } else {
            profile.setPassword(passwordEncoder.encode(newPassword));
            profileRepository.save(profile);
            result.setResult(true);
            result.setCode(HttpStatus.OK.value());
        }

        return result;
    }
}
