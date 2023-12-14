package com.kuzmich.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    private final UpdateController updateController;

    private TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message recievedMessage = update.getMessage();
        log.info(recievedMessage.getText());

        SendMessage botResponse = new SendMessage();
        botResponse.setChatId(String.valueOf(recievedMessage.getChatId()));
        botResponse.setText("Welcome");
        sendAnswerMessage(botResponse);
    }

    public void sendAnswerMessage(SendMessage message) {
        if(message != null) {
            try {
                execute(message);
            } catch (TelegramApiException ex) {
                log.error(ex.getMessage());
            }
        }
    }


    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
