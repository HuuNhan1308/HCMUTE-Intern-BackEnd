package com.intern.app.services;

import com.intern.app.models.dto.request.ProfileAuthenticationRequest;
import com.intern.app.models.dto.response.ProfileAuthenticationResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Profile;
import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.repository.ProfileRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String singerKey;

    @NonFinal
    @Value("${jwt.expirationTime}")
    protected Long expirationTime;

    ProfileRepository profileRepository;
    PasswordEncoder passwordEncoder;


    private String generateToken(Profile profile) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(profile.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + this.expirationTime))
                .claim("scope", this.buildScope(profile))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(singerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.out.print(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private String buildScope(Profile profile) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        stringJoiner.add("ROLE_" + profile.getRole().getRoleName());

        if(!CollectionUtils.isEmpty(profile.getRole().getRolePermissions())) {
            profile.getRole().getRolePermissions().forEach(permission -> {
                stringJoiner.add(permission.getPermission().getName());
            });
        }

        return stringJoiner.toString();
    }

    public ReturnResult<Boolean> IntroSpect(String token) throws ParseException, JOSEException {
        ReturnResult<Boolean> result = new ReturnResult<>();

        try {
            this.verityToken(token);
            result.setResult(true);
        } catch (AppException e) {
            result.setResult(false);
        }

        return result;
    }

    public SignedJWT verityToken(String token) throws JOSEException, ParseException {
            JWSVerifier verifier = new MACVerifier(this.singerKey.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            var verified = signedJWT.verify(verifier);

            if (!(verified && expirationTime.after(new Date()))) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }

            return signedJWT;
    }

    public ReturnResult<ProfileAuthenticationResponse> Authenticate(ProfileAuthenticationRequest profileAuthenticationRequest) {
        ReturnResult<ProfileAuthenticationResponse> result = new ReturnResult<ProfileAuthenticationResponse>();

        Profile existProfile = profileRepository.findByUsernameAndDeletedFalse(profileAuthenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAIL_CREDENTIALS));

        boolean isPwMatch = passwordEncoder.matches(profileAuthenticationRequest.getPassword(), existProfile.getPassword());

        if(!isPwMatch){
            throw new AppException(ErrorCode.LOGIN_FAIL_CREDENTIALS);
        }
        else {
            var token = this.generateToken(existProfile);

            result.setCode(HttpStatus.OK.value());
            result.setResult(
                    ProfileAuthenticationResponse.builder()
                    .isAuthenticated(true)
                    .token(token)
                    .build());
        }

        return result;
    }
}
