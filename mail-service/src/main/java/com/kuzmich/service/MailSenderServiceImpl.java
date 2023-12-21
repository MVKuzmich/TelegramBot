package com.kuzmich.service;


import com.kuzmich.dto.MailParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String activationServiceUri;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(MailParams params) {
        SimpleMailMessage mailMessage = createMessage(params);

        javaMailSender.send(mailMessage);
    }

    public SimpleMailMessage createMessage(MailParams params) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(params.getEmailTo());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To complete registration, please, pass this link: " + getActivationEmail(params));

        return mailMessage;
    }

    private String getActivationEmail(MailParams params) {
        return activationServiceUri.replace("{id}", params.getId());
    }
}
