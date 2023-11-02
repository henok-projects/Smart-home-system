package com.galsie.gcs.gcssentry.config.bootstrap;

import com.galsie.gcs.gcssentry.config.properties.GCSSentryLocalProperties;
import com.galsie.gcs.gcssentry.data.entity.microservice.GCSMicroserviceSecurityEntity;
import com.galsie.gcs.gcssentry.repository.microservice.GCSMicroserviceSecurityEntityRepository;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.serviceutilslibrary.utils.crypto.CodingAlgorithm;
import com.galsie.gcs.serviceutilslibrary.utils.crypto.Hasher;
import com.galsie.gcs.serviceutilslibrary.utils.crypto.HashingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Adds the default passwords in {@link GCSSentryLocalProperties} if none exist in the security repository
 */
@Service
@Slf4j
public class GCSSentryBootstrapService {

    @Autowired
    GCSMicroserviceSecurityEntityRepository gcsMicroserviceSecurityEntityRepository;

    @Autowired
    GCSSentryLocalProperties gcsSentryLocalProperties;

    public void bootstrap() throws GCSResponseException {
        if (gcsMicroserviceSecurityEntityRepository.count() > 0){
            return;
        }

        var entities = gcsSentryLocalProperties.getDefaultMicroservicePasswords().stream().map( password -> GCSMicroserviceSecurityEntity.builder()
                .hashedPwd(Hasher.hashValue(password, HashingAlgorithm.SHA256, CodingAlgorithm.BASE64))
                .deleted(false)
                .build()).collect(Collectors.toList());
        GCSResponse.saveEntitiesThrows(gcsMicroserviceSecurityEntityRepository, entities);
        log.info("Bootstrapped " + entities.size() + " security entities");
    }
}
