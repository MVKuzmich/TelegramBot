package com.kuzmich.service;

import com.kuzmich.dto.MailParams;
import com.kuzmich.entity.AppUser;
import com.kuzmich.entity.UserState;
import com.kuzmich.repository.AppUserRepository;
import com.kuzmich.utils.CryptoTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService {

    @Value("${service.mail.uri}")
    private String mailServiceUri;

    private final AppUserRepository appUserRepository;
    private final CryptoTool cryptoTool;

    @Override
    public String registerUser(AppUser appUser) {
        if(appUser.getIsActive()) {
            return "You have been already registered";
        } else if (appUser.getEmail() != null) {
            return "The letter was sent on your email. Please, pass the link in the letter to complete your registration!";

        }
        appUser.setUserState(UserState.WAIT_FOR_EMAIL_STATE);
        appUserRepository.save(appUser);
        return "Please, enter your email:";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddress =  new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            return "Please, enter correct email. To cancel current command send /cancel";
        }
        Optional<AppUser> appUserOptional = appUserRepository.findAppUserByEmail(email);
        if(appUserOptional.isEmpty()) {
            appUser.setEmail(email);
            appUser.setUserState(UserState.BASIC_STATE);
            appUser = appUserRepository.save(appUser);

            ResponseEntity<String> response = sendRequestToMailService(cryptoTool.toHash(appUser.getId()),
                                                                        email);
            if(response.getStatusCode() != HttpStatus.OK) {
                String errorMessage = String.format("Sending email %s is failed", email);
                log.error(errorMessage);
                appUser.setEmail(null);
                appUserRepository.save(appUser);
                return errorMessage;
            }
            return "The letter was sent to email. Pass the link to complete registration";
        } else {
            return "This email is already used. Enter correct email. To cancel current command enter /cancel";
        }


    }

    private ResponseEntity<String> sendRequestToMailService(String hash, String email) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MailParams mailParams = MailParams.builder()
                .id(hash)
                .emailTo(email)
                .build();
        HttpEntity<MailParams> request = new HttpEntity<>(mailParams, headers);

        return restTemplate.exchange(mailServiceUri,
                HttpMethod.POST,
                request,
                String.class);
    }
}
