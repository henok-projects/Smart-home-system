package com.galsie.gcs.gcssentry.controller;

import com.galsie.gcs.gcssentry.service.apiclient.LocalGCSAPIClientAuthenticatorService;
import com.galsie.gcs.gcssentry.service.microservice.LocalGCSMicroserviceAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GCSAPIClientAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GCSMicroserviceAuthenticationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/isAuthenticated")
public class GCSSentryIsAuthenticatedController {

    @Autowired
    LocalGCSMicroserviceAuthenticatorService localGCSMicroserviceAuthenticatorService;

    @Autowired
    LocalGCSAPIClientAuthenticatorService localGCSAPIClientAuthenticatorService;

    @PostMapping("/microservice")
    public ResponseEntity<?> isMicroserviceAuthenticated(GCSMicroserviceAuthenticationRequestDTO gcsMicroserviceAuthenticationRequestDTO){
        return localGCSMicroserviceAuthenticatorService.performAuthentication(gcsMicroserviceAuthenticationRequestDTO).toResponseEntity();
    }


    @PostMapping("/apiClient")
    public ResponseEntity<?> isApiClientAuthenticated(GCSAPIClientAuthenticationRequestDTO gcsapiClientAuthenticationRequestDTO){
        return localGCSAPIClientAuthenticatorService.performAuthentication(gcsapiClientAuthenticationRequestDTO).toResponseEntity();
    }
}
