package com.galsie.gcs.gcssentry.service.microservice;

import com.galsie.gcs.gcssentry.repository.microservice.GCSMicroserviceSessionEntityRepository;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice.GCSMicroserviceAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GCSMicroserviceAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LocalGCSMicroserviceAuthenticatorService extends GCSMicroserviceAuthenticator {

    @Autowired
    GCSMicroserviceSessionEntityRepository gcsMicroserviceSessionEntityRepository;

    @Override
    public GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticationRequestDTO) {
        if (!(authenticationRequestDTO instanceof GCSMicroserviceAuthenticationRequestDTO gcsMicroserviceAuthenticationRequestDTO)){
            return GCSResponse.errorResponse(GCSResponseErrorType.MISMATCH_ERROR);
        }
        var token = gcsMicroserviceAuthenticationRequestDTO.getToken();
        var sessionEntityOpt = gcsMicroserviceSessionEntityRepository.findBySessionToken(token);
        if (sessionEntityOpt.isEmpty()){
            return AuthenticationResponseDTO.responseError(AuthenticationResponseErrorType.MICROSERVICE_AUTH_TOKEN_INVALID);
        }
        var sessionEntity = sessionEntityOpt.get();
        if (sessionEntity.isExpired()){
            return AuthenticationResponseDTO.responseError(AuthenticationResponseErrorType.MICROSERVICE_AUTH_SESSION_EXPIRED);
        }
        sessionEntity.updateLastAccess();
        var response = GCSResponse.saveEntity(gcsMicroserviceSessionEntityRepository, sessionEntity);
        if (response.hasError()){
            return GCSResponse.errorResponse(response.getGcsError());
        }
        return AuthenticationResponseDTO.responseSuccess();
    }

    @Override
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO) {
        return GCSResponse.errorResponse(GCSResponseErrorType.NOT_IMPLEMENTED);
    }
}
