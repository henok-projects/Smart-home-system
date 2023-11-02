package com.galsie.gcs.email.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.email.service.GCSEmailSenderService;
import com.galsie.gcs.microservicecommon.lib.email.data.dto.request.SendEmailRequestDTO;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.GCSRabbitCommonQueueConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class GCSEmailsRabbitConsumer {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GCSEmailSenderService gcsEmailSenderService;

    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.EMAILS)
    public void consumeEmailRequest(Message message){
        var sendEmailDTOOpt = fromMessage(message, SendEmailRequestDTO.class);
        if (sendEmailDTOOpt.isEmpty()){
            log.info("Invalid Email message received..");
            return;
        }
        var sendEmailDTO = sendEmailDTOOpt.get();
        this.sendEmail(sendEmailDTO);

    }

    private void sendEmail(SendEmailRequestDTO sendEmailDTO){
        var response = gcsEmailSenderService.sendEmail(sendEmailDTO);
        if (response.hasError()){
            log.info("Failed to send email, reason: " + response.getGcsError().getErrorType().name());
            return;
        }
        var responseDTO = response.getResponseData();
        if (responseDTO.hasError()){
            log.info("Failed to send email, reason: " + responseDTO.getSendEmailResponseError().name());
        }
        log.info("Email to " + sendEmailDTO + " of type " + sendEmailDTO.getEmailType().name() + " was sent!");
    }

    private <T> Optional<T> fromMessage(Message message, Class<T> tClass){
        try {
            return Optional.of(objectMapper.readValue(message.getBody(), tClass));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
