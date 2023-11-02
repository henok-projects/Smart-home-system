package com.galsie.gcs.gcssentry;

import com.galsie.gcs.microservicecommon.GCSMicroserviceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GCSSentryApplication extends GCSMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GCSSentryApplication.class, args);
	}

}
