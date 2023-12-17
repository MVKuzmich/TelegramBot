package com.kuzmich.service;

import com.kuzmich.entity.AppUser;
import com.kuzmich.entity.RawData;
import com.kuzmich.entity.UserState;
import com.kuzmich.repository.AppUserRepository;
import com.kuzmich.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {

    private final RawDataRepository rawDataRepository;
    private final ProducerService producerService;
    private final AppUserRepository appUserRepository;
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        Message textMessage = update.getMessage();
        User telegramUser = textMessage.getFrom();
        findOrSaveAppUser(telegramUser);


        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from NODE");

        producerService.produceAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder().event(update).build();
        rawDataRepository.save(rawData);
    }

    private AppUser findOrSaveAppUser(User telegramUser) {
        Optional<AppUser> persistentAppUser = appUserRepository.findAppUserByTelegramUserId(telegramUser.getId());
        if(persistentAppUser.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .userState(UserState.BASIC_STATE)
                    .build();
            return appUserRepository.save(transientAppUser);
        } else {
            return persistentAppUser.get();
        }

    }
}
