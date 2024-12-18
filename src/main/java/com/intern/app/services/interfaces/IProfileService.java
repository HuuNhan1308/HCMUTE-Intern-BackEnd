package com.intern.app.services.interfaces;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.request.ChangePasswordRequest;
import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Role;
import org.springframework.http.HttpStatus;

public interface IProfileService {
    ReturnResult<Profile> CreateUser(ProfileCreationRequest request, Role role);

    ReturnResult<Boolean> ChangePassword(ChangePasswordRequest changePasswordRequest);
    ReturnResult<Boolean> ChangePassword(String newPassword, String profileId);
}
