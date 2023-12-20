package com.kuzmich.service;

import com.kuzmich.entity.*;
import com.kuzmich.enums.ServiceCommand;
import com.kuzmich.exceptions.UploadFileException;
import com.kuzmich.repository.AppUserRepository;
import com.kuzmich.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kuzmich.entity.UserState.BASIC_STATE;
import static com.kuzmich.entity.UserState.WAIT_FOR_EMAIL_STATE;
import static com.kuzmich.enums.ServiceCommand.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class MainServiceImpl implements MainService {

    private final RawDataRepository rawDataRepository;
    private final ProducerService producerService;
    private final AppUserRepository appUserRepository;
    private final FileService fileService;
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getUserState();
        String messageText = update.getMessage().getText();
        String response = "";

        ServiceCommand serviceCommand = ServiceCommand.fromText(messageText);
        if(CANCEL.equals(serviceCommand)) {
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
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        String chatId = update.getMessage().getChatId().toString();
        if(isNotAllowedSendContent(chatId, appUser)) {
            return;
        }

        try {
            AppDocument document = fileService.processDoc(update.getMessage());
            //TODO add algorithm for link building to download document
            String response = "The document is uploaded successfully! The link for downloading: https://test.ru/document/777";
            sendAnswer(response, chatId);
        } catch (UploadFileException ex) {
            log.info(ex.getMessage());
            String errorMessage = "Unfortunately, file is not downloaded. Make an attempt later.";
            sendAnswer(errorMessage, chatId);
        }

    }

    private boolean isNotAllowedSendContent(String chatId, AppUser appUser) {
        UserState userState = appUser.getUserState();
        if(!appUser.getIsActive()) {
            String response = "To register or sign in in order to upload content!";
            sendAnswer(response, chatId);
            return true;
        } else if(!BASIC_STATE.equals(userState)) {
            String response = "This action is not allowed. PLease, cancel current command for file uploading via /cancel command.";
            sendAnswer(response, chatId);
            return true;
        }
        return false;
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        String chatId = update.getMessage().getChatId().toString();
        if(isNotAllowedSendContent(chatId, appUser)) {
            return;
        }
        try {
            AppPhoto appPhoto = fileService.processPhoto(update.getMessage());
            //TODO add algorithm for link building to download photo
            String response = "The photo is uploaded successfully! The link for downloading: https://test.ru/photo/777";
            sendAnswer(response, chatId);
        } catch(UploadFileException ex) {
            log.info(ex.getMessage());
            String errorMessage = "Unfortunately, photo is not downloaded. Make an attempt later.";
            sendAnswer(errorMessage, chatId);
        }

    }

    @Override
    public void processDiceMessage(Update update) {
        saveRawData(update);
        String chatId = update.getMessage().getChatId().toString();
        String response = String.format("Dice has number %s", update.getMessage().getDice().getValue());
        sendAnswer(response, chatId);
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
        String commandList = Arrays.stream(ServiceCommand.values())
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
