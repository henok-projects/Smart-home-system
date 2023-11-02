package com.galsie.gcs.gcssentry.data.entity.apiclient;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.entity.GalSecurityCommonSessionEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * AN APIClientKeyEntity is a standalone session where the token is the api key
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class APIClientKeyEntity extends GalSecurityCommonSessionEntity implements GalEntity<Long> {

    @Builder
    public APIClientKeyEntity(Long id, String apiKey, boolean expired, LocalDateTime validUntil, LocalDateTime lastAccess,
                              LocalDateTime createdAt, LocalDateTime modifiedAt) {
        super(apiKey, expired, validUntil, lastAccess, createdAt, modifiedAt);
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

}
