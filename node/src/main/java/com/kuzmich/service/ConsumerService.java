package com.kuzmich.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {

    void consumeTextMessageUpdates(Update update);
    void consumeDocMessageUpdates(Update update);
    void consumePhotoMessageUpdates(Update update);
    void consumeDiceMessageUpdates(Update update);

}
