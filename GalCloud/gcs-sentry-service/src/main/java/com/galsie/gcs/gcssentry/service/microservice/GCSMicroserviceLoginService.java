package com.galsie.gcs.gcssentry.service.microservice;

import com.galsie.gcs.gcssentry.data.entity.microservice.GCSMicroserviceSecurityEntity;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.response.GCSMicroserviceLoginResponseErrorType;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.request.GCSMicroserviceLoginRequestDTO;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.response.GCSMicroserviceLoginResponseDTO;
import com.galsie.gcs.gcssentry.data.entity.microservice.GCSMicroserviceSessionEntity;
import com.galsie.gcs.gcssentry.repository.microservice.GCSMicroserviceSecurityEntityRepository;
import com.galsie.gcs.gcssentry.repository.microservice.GCSMicroserviceSessionEntityRepository;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice.CodableGCSMicroserviceAuthSessionToken;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class GCSMicroserviceLoginService {
    @Autowired
    GCSMicroserviceSecurityEntityRepository gcsMicroserviceSecurityEntityRepository;

    @Autowired
    GCSMicroserviceSessionEntityRepository gcsMicroserviceSessionEntityRepository;

    public GCSResponse<GCSMicroserviceLoginResponseDTO> gcsMicroserviceLogin(GCSMicroserviceLoginRequestDTO gcsMicroserviceLoginRequestDTO) {
        var securityEntityOpt = gcsMicroserviceSecurityEntityRepository.findAllByHashedPwd(gcsMicroserviceLoginRequestDTO.getHashedPwd()).stream().filter((securityEntity) -> !securityEntity.isDeleted()).findFirst();
        if (securityEntityOpt.isEmpty()) {
            return GCSMicroserviceLoginResponseDTO.responseError(GCSMicroserviceLoginResponseErrorType.INVALID_CREDENTIALS);
        }
        var securityEntity = securityEntityOpt.get(); // wouldn't be deleted since we filtered above
        var sessionEntity = GCSMicroserviceSessionEntity.builder().securityEntity(securityEntity).expired(false).validUntil(LocalDateTime.now().plusHours(GCSMicroserviceSessionEntity.SESSION_VALIDITY_HOURS)).build();
        var saveSessionEntityResponse = GCSResponse.saveEntity(gcsMicroserviceSessionEntityRepository, sessionEntity);
        if (saveSessionEntityResponse.hasError()){
            return GCSResponse.errorResponse(saveSessionEntityResponse.getGcsError());
        }
        sessionEntity = saveSessionEntityResponse.getResponseData();
        var serviceName = gcsMicroserviceLoginRequestDTO.getServiceName();
        var codableToken = new CodableGCSMicroserviceAuthSessionToken(sessionEntity.getId(), serviceName);
        sessionEntity.setSessionToken(codableToken.toStringToken());

        saveSessionEntityResponse = GCSResponse.saveEntity(gcsMicroserviceSessionEntityRepository, sessionEntity);
        if (saveSessionEntityResponse.hasError()){
            return GCSResponse.errorResponse(saveSessionEntityResponse.getGcsError());
        }
        sessionEntity = saveSessionEntityResponse.getResponseData();
        return GCSMicroserviceLoginResponseDTO.responseSuccess(sessionEntity.getSessionToken());
    }

}
