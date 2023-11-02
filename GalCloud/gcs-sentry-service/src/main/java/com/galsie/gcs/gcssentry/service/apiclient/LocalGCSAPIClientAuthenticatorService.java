package com.galsie.gcs.gcssentry.service.apiclient;

import com.galsie.gcs.gcssentry.data.entity.apiclient.APIClientKeyEntity;
import com.galsie.gcs.gcssentry.repository.apiclient.APIClientKeyEntityRepository;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice.GCSMicroserviceAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GCSAPIClientAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocalGCSAPIClientAuthenticatorService extends GCSMicroserviceAuthenticator {

    @Autowired
    APIClientKeyEntityRepository apiClientKeyEntityRepository;

    @Override
    public GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticationRequestDTO) {
        if (!(authenticationRequestDTO instanceof GCSAPIClientAuthenticationRequestDTO gcsapiClientAuthenticationRequestDTO)){
            return GCSResponse.errorResponse(GCSResponseErrorType.MISMATCH_ERROR);
        }
        var apiKey = gcsapiClientAuthenticationRequestDTO.getApiKey();
        Optional<APIClientKeyEntity> apiClientKeyEntityRepositoryOpt = apiClientKeyEntityRepository.findBySessionToken(apiKey);
        if (apiClientKeyEntityRepositoryOpt.isEmpty()){
            return AuthenticationResponseDTO.responseError(AuthenticationResponseErrorType.API_KEY_INVALID);
        }
        var apiClientKeyEntity = apiClientKeyEntityRepositoryOpt.get();
        if (apiClientKeyEntity.isExpired()){
            return AuthenticationResponseDTO.responseError(AuthenticationResponseErrorType.API_KEY_EXPIRED);
        }
        apiClientKeyEntity.updateLastAccess();
        var saveEntityResponse = GCSResponse.saveEntity(apiClientKeyEntityRepository, apiClientKeyEntity);
        if (saveEntityResponse.hasError()){
            return GCSResponse.errorResponse(saveEntityResponse.getGcsError());
        }
        return AuthenticationResponseDTO.responseSuccess();
    }

    @Override
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO) {
        return GCSResponse.errorResponse(GCSResponseErrorType.NOT_IMPLEMENTED);
    }
}
