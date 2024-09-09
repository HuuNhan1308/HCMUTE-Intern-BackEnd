package com.intern.app.controller;

import com.intern.app.dto.request.ProfileAuthenticationRequest;
import com.intern.app.dto.request.ProfileCreationRequest;
import com.intern.app.dto.request.TokenRequest;
import com.intern.app.dto.response.ProfileAuthenticationResponse;
import com.intern.app.dto.response.ProfileResponse;
import com.intern.app.dto.response.ReturnResult;
import com.intern.app.services.AuthenticationService;
import com.intern.app.services.ProfileService;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @PostMapping("/CreateProfile")
    public ResponseEntity<ReturnResult<Boolean>> CreateProfile(@RequestBody ProfileCreationRequest profile) {
        ReturnResult<Boolean> result = profileService.CreateUser(profile);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ReturnResult<ProfileResponse>> GetProfileById(@PathVariable String profileId) {
        ReturnResult<ProfileResponse> result = profileService.FindProfileById(profileId);

        return ResponseEntity.ok().body(result);
    }
}
