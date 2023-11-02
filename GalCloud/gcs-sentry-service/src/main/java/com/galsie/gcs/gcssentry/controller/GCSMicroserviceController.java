package com.galsie.gcs.gcssentry.controller;

import com.galsie.gcs.microservicecommon.data.dto.microservice.login.request.GCSMicroserviceLoginRequestDTO;
import com.galsie.gcs.gcssentry.service.microservice.GCSMicroserviceLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gcsMicroservice")
public class GCSMicroserviceController {

    @Autowired
    GCSMicroserviceLoginService gcsMicroserviceLoginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody GCSMicroserviceLoginRequestDTO gcsMicroserviceLoginRequestDTO){
        return gcsMicroserviceLoginService.gcsMicroserviceLogin(gcsMicroserviceLoginRequestDTO).toResponseEntity();
    }
}
