package com.galsie.gcs.gcssentry.config.security;

import com.galsie.gcs.gcssentry.service.microservice.LocalGCSMicroserviceAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.AutoConfiguredGalSecurityRequestsFilter;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.GalSecurityRequestsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GCSSentrySecurityConfiguration extends GalSecurityConfigurationWithCommonItems {
    @Autowired
    LocalGCSMicroserviceAuthenticatorService localGCSMicroserviceAuthenticatorService;

    @Override
    public List<GalSecurityRequestsFilter> getRequestFilters() {
        var microserviceAuthenticatorFilter = new AutoConfiguredGalSecurityRequestsFilter(GalSecurityAuthSessionType.GCS_MICROSERVICE, localGCSMicroserviceAuthenticatorService);
        var apiClientAuthenticatorFilter = new AutoConfiguredGalSecurityRequestsFilter(GalSecurityAuthSessionType.GCS_API_CLIENT, localGCSMicroserviceAuthenticatorService);
        return Arrays.asList(microserviceAuthenticatorFilter, apiClientAuthenticatorFilter);
    }

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return new ArrayList<>(); // No local caching
    }

    @Override
    public boolean isMicroserviceAutoLoginWithSentryEnabled(){
        return false; // this microservice should not login with the sentry
    }

}
