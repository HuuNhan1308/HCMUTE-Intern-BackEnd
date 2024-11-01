package com.intern.app.services.interfaces;

import com.intern.app.models.dto.request.ProfileAuthenticationRequest;
import com.intern.app.models.dto.response.ProfileAuthenticationResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Profile;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;


public interface IAuthenticationService {
    ReturnResult<Boolean> IntroSpect(String token) throws ParseException, JOSEException;

    SignedJWT verityToken(String token) throws ParseException, JOSEException;

    ReturnResult<ProfileAuthenticationResponse> Authenticate(ProfileAuthenticationRequest profileAuthenticationRequest);

    ReturnResult<Boolean> IsContainPermission(Profile profile, String permissionName);
}
