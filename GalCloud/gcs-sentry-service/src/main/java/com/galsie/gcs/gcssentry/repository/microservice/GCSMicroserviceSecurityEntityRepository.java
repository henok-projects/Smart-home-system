package com.galsie.gcs.gcssentry.repository.microservice;

import com.galsie.gcs.gcssentry.data.entity.microservice.GCSMicroserviceSecurityEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GCSMicroserviceSecurityEntityRepository extends GalRepository<GCSMicroserviceSecurityEntity, Long> {

    List<GCSMicroserviceSecurityEntity> findAllByHashedPwd(String hashedPwd);

}
