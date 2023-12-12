package com.kuzmich.controller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());

    }

    @Override
    public String getBotToken() {
        return "6925743259:AAFK1B4neEZ545CiEfhpw96faK7wUpaWikE";
    }

    @Override
    public String getBotUsername() {
        return "KuzmichAppBot";
    }
}
