package com.galsie.gcs.gcssentry.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Getter
public class GCSSentryLocalProperties {
    @Value("${galsie.sentry.default-microservice-passwords}")
    List<String> defaultMicroservicePasswords;
}
