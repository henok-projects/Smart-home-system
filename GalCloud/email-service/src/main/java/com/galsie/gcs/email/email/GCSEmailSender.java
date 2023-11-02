package com.galsie.gcs.email.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class GCSEmailSender {

    private final Session emailSession;
    private final String username;
    public GCSEmailSender(String emailSmtpHost, String smtpPort, String username, String password){
        this.username = username;
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", emailSmtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        this.emailSession = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public boolean sendHTMLEmail(String fromName, String toAddress, String subject, String htmlContent){
        Message message = new MimeMessage(emailSession);
        try {
            message.setFrom(new InternetAddress(this.username, fromName));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace(); // TODO: Log
            return false;
        }
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
