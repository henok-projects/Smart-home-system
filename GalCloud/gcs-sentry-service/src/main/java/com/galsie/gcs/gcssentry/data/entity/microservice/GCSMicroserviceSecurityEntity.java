package com.galsie.gcs.gcssentry.data.entity.microservice;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GCSMicroserviceSecurityEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    /*
    TODO: USE certificates issued to microservices instead of passwords
     */
    @Column
    String hashedPwd;

    @Column
    boolean deleted;
}
