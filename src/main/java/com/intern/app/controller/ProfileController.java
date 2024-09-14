package com.intern.app.controller;

import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.ProfileService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/profile")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ProfileController {
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
