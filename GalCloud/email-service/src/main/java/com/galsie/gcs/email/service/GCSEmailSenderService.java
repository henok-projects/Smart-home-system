package com.galsie.gcs.email.service;

import com.galsie.gcs.email.email.HTMLEmailModelManager;
import com.galsie.gcs.microservicecommon.lib.email.data.discrete.SendEmailResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.email.data.dto.request.SendEmailRequestDTO;
import com.galsie.gcs.email.email.GCSEmailSender;
import com.galsie.gcs.microservicecommon.lib.email.data.dto.response.SendEmailResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GCSEmailSenderService {


    @Autowired
    GCSEmailSender emailSender;

    @Autowired
    HTMLEmailModelManager htmlEmailModelManager;

    public GCSResponse<SendEmailResponseDTO> sendEmail(SendEmailRequestDTO emailRequestDTO){
        var emailType = emailRequestDTO.getEmailType();
        var modelOpt = htmlEmailModelManager.getEmailModelFor(emailType);
        if (modelOpt.isEmpty()){
            return SendEmailResponseDTO.responseError(SendEmailResponseErrorType.HTML_EMAIL_MODEL_NOT_FOUND);
        }
        var htmlText  = modelOpt.get().getReplacedHTML(emailRequestDTO.getVariableReplacement());
        try {
            emailSender.sendHTMLEmail(emailType.getFromName(), emailRequestDTO.getToAddress(), emailType.getSubject(), htmlText);
        }catch (Exception exception){
            return SendEmailResponseDTO.responseError(SendEmailResponseErrorType.EMAIL_SENDER_FAILED);
        }
        return SendEmailResponseDTO.responseSuccess();
    }

}
