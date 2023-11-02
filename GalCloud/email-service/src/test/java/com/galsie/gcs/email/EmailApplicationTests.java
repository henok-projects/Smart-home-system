package com.galsie.gcs.email;

import com.galsie.gcs.microservicecommon.lib.email.data.discrete.EmailType;
import com.galsie.gcs.microservicecommon.lib.email.data.dto.request.SendEmailRequestDTO;
import com.galsie.gcs.email.service.GCSEmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class EmailApplicationTests {

	@Autowired
	GCSEmailSenderService emailSenderService;

	@Test
	void contextLoads() {
	}

	@Test
	void sendEmail(){
		var response = emailSenderService.sendEmail(new SendEmailRequestDTO(EmailType.USER_EMAIL_VERIFICATION, "liwaa@galsie.com", new HashMap<>()));
		assert !response.hasError();
		var data = response.getResponseData();
		assert !data.hasError();
	}

}
