package com.galsie.gcs.email.configuration.email;


import com.galsie.gcs.email.email.GCSEmailSender;
import com.galsie.gcs.email.email.HTMLEmailModelManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GCSEmailConfiguration {

    @Value("${galsie.email.smtp.host}")
    String smtpHost;

    @Value("${galsie.email.smtp.port}")
    String smtpPort;

    @Value("${galsie.email.username}")
    String username;

    @Value("${galsie.email.password}")
    String password;

    @Bean
    public GCSEmailSender galEmailSender() {
        return new GCSEmailSender(smtpHost, smtpPort, username, password);
    }

    @Bean
    public HTMLEmailModelManager htmlEmailModelManager(){
        return new HTMLEmailModelManager();
    }
}
