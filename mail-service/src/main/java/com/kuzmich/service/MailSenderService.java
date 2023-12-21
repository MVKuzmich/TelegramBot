package com.kuzmich.service;

import com.kuzmich.dto.MailParams;

public interface MailSenderService {

    void sendEmail(MailParams params);


}
