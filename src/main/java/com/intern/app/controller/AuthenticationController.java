package com.intern.app.controller;


import com.intern.app.models.dto.request.ProfileAuthenticationRequest;
import com.intern.app.models.dto.request.TokenRequest;
import com.intern.app.models.dto.response.ProfileAuthenticationResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/authentication")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/Authenticate")
    public ResponseEntity<ReturnResult<ProfileAuthenticationResponse>> Login(@RequestBody ProfileAuthenticationRequest profileAuthenticationRequest) {
        ReturnResult<ProfileAuthenticationResponse> result = this.authenticationService.Authenticate(profileAuthenticationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/IntroSpect")
    public ResponseEntity<ReturnResult<Boolean>> IntroSpect(@RequestBody TokenRequest token) throws ParseException, JOSEException {
        ReturnResult<Boolean> result = this.authenticationService.IntroSpect(token.getToken());

        return ResponseEntity.ok().body(result);
    }
}
