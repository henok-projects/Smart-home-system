package com.galsie.gcs.continuousservice.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class ContinuousSocketsLocalProperties {

    /**
     * Paths to connect to sockets
     */
    @Value("${galsie.sockets.connection-paths.user}")
    private String mobileSocketPath;

    @Value("${galsie.sockets.connection-paths.galdevice}")
    private String galDeviceSocketPath;

    @Value("${galsie.sockets.connection-paths.test}")
    private String testSocketPath;


    /**
     * Paths requiring security authentication
     */
    @Value("${galsie.sockets.security-auth-paths.user}")
    private List<String> userSocketAuthPaths;

    @Value("${galsie.sockets.security-auth-paths.galdevice}")
    private List<String> galDeviceSocketAuthPaths;

    @Value("${galsie.sockets.security-auth-paths.test}")
    private List<String> testSocketAuthPaths;

}
