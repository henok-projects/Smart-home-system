package com.galsie.gcs.gcssentry.data.entity.microservice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.entity.GalSecurityCommonSessionEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class GCSMicroserviceSessionEntity extends GalSecurityCommonSessionEntity implements GalEntity<Long> {

    @Builder
    public GCSMicroserviceSessionEntity(Long id, GCSMicroserviceSecurityEntity securityEntity, String authToken, boolean expired, LocalDateTime validUntil, LocalDateTime lastAccess,
                                        LocalDateTime createdAt, LocalDateTime modifiedAt) {
        super(authToken, expired, validUntil, lastAccess, createdAt, modifiedAt);
        this.id = id;
        this.GCSMicroserviceSecurityEntity = securityEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private GCSMicroserviceSecurityEntity GCSMicroserviceSecurityEntity;

}
