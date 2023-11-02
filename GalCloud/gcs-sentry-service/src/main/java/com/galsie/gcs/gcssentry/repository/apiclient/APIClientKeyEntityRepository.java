package com.galsie.gcs.gcssentry.repository.apiclient;

import com.galsie.gcs.gcssentry.data.entity.apiclient.APIClientKeyEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface APIClientKeyEntityRepository extends GalRepository<APIClientKeyEntity, Long> {

    Optional<APIClientKeyEntity> findBySessionToken(String sessionToken);
}
