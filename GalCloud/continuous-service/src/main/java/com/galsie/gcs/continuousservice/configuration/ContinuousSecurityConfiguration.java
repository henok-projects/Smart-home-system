package com.galsie.gcs.continuousservice.configuration;

import com.galsie.gcs.continuousservice.service.TestAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.GalSecurityRequestsFilter;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.ManuallyConfiguredGalSecurityRequestsFilter;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.galdevice.RemoteGalDeviceAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.RemoteUserAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ContinuousSecurityConfiguration extends GalSecurityConfiguration {

    @Autowired
    RemoteUserAuthenticatorService remoteUserAuthenticatorService;

    @Autowired
    RemoteGalDeviceAuthenticatorService remoteGalDeviceAuthenticator;

    @Autowired
    TestAuthenticatorService testAuthenticatorService;

    @Autowired
    ContinuousSocketsLocalProperties continuousSocketsLocalProperties;


    @Override
    public HttpSecurity setupHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // disable basic http for sockets
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
    }

    @Override
    public List<GalSecurityRequestsFilter> getRequestFilters() {
        var userAuthRequestsFilter = new ManuallyConfiguredGalSecurityRequestsFilter(continuousSocketsLocalProperties.getUserSocketAuthPaths(), remoteUserAuthenticatorService);
        var galdeviceAuthRequestsFilter = new ManuallyConfiguredGalSecurityRequestsFilter(continuousSocketsLocalProperties.getGalDeviceSocketAuthPaths(), remoteGalDeviceAuthenticator);
        var testAuthRequestFilter = new ManuallyConfiguredGalSecurityRequestsFilter(continuousSocketsLocalProperties.getTestSocketAuthPaths(), testAuthenticatorService);

        // can add as many filters as needed, for as many purposes as needed.
        return Arrays.asList(userAuthRequestsFilter, galdeviceAuthRequestsFilter, testAuthRequestFilter);
    }

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return Arrays.asList(GalSecurityAuthSessionType.USER, GalSecurityAuthSessionType.GCS_API_CLIENT, GalSecurityAuthSessionType.GALDEVICE, GalSecurityAuthSessionType.GCS_MICROSERVICE);
    }

}
