package com.galsie.gcs.email.configuration.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfiguration;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQQueueConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GCSEmailRabbitMQConfiguration extends GCSRabbitMQConfiguration {


    @Override
    public List<GCSRabbitMQQueueConfiguration> getExchangeBindingConfigurations() {
        var emailsQueueConfig = GCSRabbitMQQueueConfiguration.builder()
                .commonQueueType(GCSRabbitMQCommonQueueType.EMAILS)
                .isDurable(true)
                .isExclusive(false)
                .autoDelete(false)
                .build();

        return Arrays.asList(emailsQueueConfig);
    }
}
