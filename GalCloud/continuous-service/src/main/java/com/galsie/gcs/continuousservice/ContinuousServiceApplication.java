package com.galsie.gcs.continuousservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.galsie.gcs.continuousservice", "com.galsie.gcs.microservicecommon", "com.galsie.gcs.serviceutilslibrary"})
@EntityScan(basePackages = {"com.galsie.gcs.continuousservice", "com.galsie.gcs.microservicecommon", "com.galsie.gcs.serviceutilslibrary"} )
@EnableJpaRepositories(basePackages = {"com.galsie.gcs.continuousservice", "com.galsie.gcs.microservicecommon","com.galsie.gcs.serviceutilslibrary"})
public class ContinuousServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContinuousServiceApplication.class, args);
	}

}
