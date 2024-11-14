package com.intern.app.controller;

import com.intern.app.models.dto.request.ChangePasswordRequest;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.implement.ProfileService;

import com.intern.app.services.interfaces.IProfileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/profile")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProfileController {
    IProfileService profileService;

    @PostMapping("/ChangePassword")
    public ResponseEntity<ReturnResult<Boolean>> ChangePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        ReturnResult<Boolean> result = profileService.ChangePassword(changePasswordRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/AdminChangePassword/{profileId}")
    public ResponseEntity<ReturnResult<Boolean>> AdminChangePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @PathVariable String profileId) {
        ReturnResult<Boolean> result = profileService.ChangePassword(changePasswordRequest, profileId);

        return ResponseEntity.ok().body(result);
    }
}
