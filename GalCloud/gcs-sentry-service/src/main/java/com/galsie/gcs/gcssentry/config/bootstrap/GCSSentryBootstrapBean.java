package com.galsie.gcs.gcssentry.config.bootstrap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GCSSentryBootstrapBean implements InitializingBean {

    @Autowired
    GCSSentryBootstrapService service;

    @Override
    public void afterPropertiesSet() throws Exception {
        service.bootstrap();
    }
}

