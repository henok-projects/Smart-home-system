package com.galsie.gcs.certauthority.config.security;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.GalSecurityRequestsFilter;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.ManuallyConfiguredGalSecurityRequestsFilter;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Certificate Authority Security configuration
 *
 *
 * Implementation:
 * - A Bean is created by the parent class {@link GalSecurityConfiguration} that filters by the filters defined in {@link #getRequestFilters()}
 *
 * {@inheritDoc}
 * Inherits {@link GalSecurityConfiguration} to comply with the micro-service-common security architecture
 */
@Configuration
public class CertAuthSecurityConfiguration extends GalSecurityConfigurationWithCommonItems {

    @Override
    public List<GalSecurityRequestsFilter> getRequestFilters() {

        // can add as many filters as needed, for as many purposes as needed.
        return getAutoConfiguredAndRemoteAuthenticatedRequestFiltersFor(GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT, GalSecurityAuthSessionType.USER, GalSecurityAuthSessionType.GALDEVICE);
    }

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return Arrays.asList(GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT, GalSecurityAuthSessionType.USER, GalSecurityAuthSessionType.GALDEVICE);
    }
}
