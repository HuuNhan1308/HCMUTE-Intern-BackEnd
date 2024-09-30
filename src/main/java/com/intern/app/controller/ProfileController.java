package com.intern.app.controller;

import com.intern.app.models.dto.request.ChangePasswordRequest;
import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.ProfileService;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping(path = "/api/profile")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @GetMapping("/{profileId}")
    public ResponseEntity<ReturnResult<ProfileResponse>> GetProfileById(@PathVariable String profileId) {
        ReturnResult<ProfileResponse> result = profileService.FindProfileById(profileId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/ChangePassword")
    public ResponseEntity<ReturnResult<Boolean>> ChangePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        ReturnResult<Boolean> result = profileService.ChangePassword(
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword(),
                username);
        return ResponseEntity.ok().body(result);
    }
}
