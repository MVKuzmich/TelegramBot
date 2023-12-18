package com.kuzmich.service;

import com.kuzmich.entity.AppUser;
import com.kuzmich.entity.RawData;
import com.kuzmich.entity.UserState;
import com.kuzmich.enums.ServiceCommands;
import com.kuzmich.repository.AppUserRepository;
import com.kuzmich.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kuzmich.entity.UserState.*;
import static com.kuzmich.enums.ServiceCommands.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class MainServiceImpl implements MainService {

    private final RawDataRepository rawDataRepository;
    private final ProducerService producerService;
    private final AppUserRepository appUserRepository;
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getUserState();
        String messageText = update.getMessage().getText();
        String response = "";

        if(CANCEL.equals(messageText)) {
            response = cancelProcess(appUser);
        } else if(BASIC_STATE.equals(userState)) {
            response = processServiceCommand(appUser, messageText);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO добавить обработку email

        } else {
            log.info("Unknown user state " + userState);
            response = "Unknown error! Enter /cancel and try again!";
        }

        String chatId = update.getMessage().getChatId().toString();
        sendAnswer(response, chatId);


        Message message = update.getMessage();

    }

    private void sendAnswer(String response, String chatId) {
        producerService.produceAnswer(new SendMessage(chatId, response));
    }

    private String processServiceCommand(AppUser appUser, String messageText) {
        if(REGISTRATION.equals(messageText)) {
            //TODO добавить функционал по регистрации
            return "It's temporarily inaccessible.";
        } else if(HELP.equals(messageText)) {
            return allCommandsList();
        } else if(START.equals(messageText)) {
            return "Welcome! To receive list of commands please enter /help.";
        } else {
            return "Unknown command! To receive list of commands please enter /help.";
        }
    }

    private String allCommandsList() {
        String commandList = Arrays.stream(ServiceCommands.values())
                .map(cmd -> cmd.getCommand() + " - " + cmd.getDescription())
                .collect(Collectors.joining("\n"));
        return String.format("List of commands:\n%s", commandList);
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setUserState(BASIC_STATE);
        appUserRepository.save(appUser);

        return "Command is cancelled";
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder().event(update).build();
        rawDataRepository.save(rawData);
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        Optional<AppUser> persistentAppUser = appUserRepository.findAppUserByTelegramUserId(telegramUser.getId());
        if(persistentAppUser.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .userState(BASIC_STATE)
                    .build();
            return appUserRepository.save(transientAppUser);
        } else {
            return persistentAppUser.get();
        }

    }
}
