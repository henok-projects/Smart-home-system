package com.galsie.gcs.continuousservice.service;

import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.TokenAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

@Service
public class TestAuthenticatorService extends GalSecurityAuthenticator {
    @Override
    public GalSecurityAuthSessionType galSecurityAuthSessionType() {
        return GalSecurityAuthSessionType.TEST;
    }

    @Override
    public Optional<? extends AuthenticationRequestDTO> getAuthenticationRequestDTO(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader("authToken"); // TODO: Load token key identifier from configuration
        if (authToken == null){
            return Optional.empty();
        }
        return Optional.of(new TestAuthenticationRequestDTO(authToken));
    }

    @Override
    public GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticationRequestDTO) {
        if (!(authenticationRequestDTO instanceof TokenAuthenticationRequestDTO tokenAuthenticationRequestDTO)){
            return GCSResponse.errorResponseWithMessage(GCSResponseErrorType.MISMATCH_ERROR, "Mismatch between the expected DTO and the passed one.");
        }
        var sessionToken = tokenAuthenticationRequestDTO.getToken();
        if (sessionToken.contains("test")){
            return AuthenticationResponseDTO.responseSuccess();
        }
        return AuthenticationResponseDTO.responseError(AuthenticationResponseErrorType.TEST_AUTH_TOKEN_INVALID);
    }

    @Override
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO) {
        return null;
    }

    @Override
    public Optional<? extends GalSecurityAuthSession<?>> createAuthSession(AuthenticationRequestDTO authenticationRequestDTO, AuthenticationResponseDTO authenticationResponseDTO) {
        if (!(authenticationRequestDTO instanceof TokenAuthenticationRequestDTO tokenAuthRequestDTO)){
            return Optional.empty();
        }
        if (!authenticationResponseDTO.isAuthenticated()){
            return Optional.empty();
        }
        var session = new TestAuthSession("test", tokenAuthRequestDTO.getToken());
        return Optional.of(session);
    }

    class TestAuthenticationRequestDTO extends TokenAuthenticationRequestDTO {
        public TestAuthenticationRequestDTO(String token){
            super(token);
        }
    }

    class TestAuthSession extends GalSecurityAuthSession<String>{

        public TestAuthSession(String entityIdentifier, String token) {
            super(entityIdentifier, token);
        }

        @Override
        public GalSecurityAuthSessionType getSecurityAuthSessionType() {
            return GalSecurityAuthSessionType.TEST;
        }

        @Override
        public HashMap<String, String> getHeaderParameters() {
            return null;
        }
    }
}
