package com.galsie.gcs.email.configuration.security;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.GalSecurityRequestsFilter;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GCSEmailSecurityConfiguration extends GalSecurityConfigurationWithCommonItems {
    @Override
    public List<GalSecurityRequestsFilter> getRequestFilters() {
        var filters = new ArrayList<GalSecurityRequestsFilter>();
        filters.addAll(this.getAutoConfiguredAndRemoteAuthenticatedRequestFiltersFor(GalSecurityAuthSessionType.GCS_MICROSERVICE));
        return filters;
    }

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return Arrays.asList(GalSecurityAuthSessionType.GCS_MICROSERVICE);
    }
}
