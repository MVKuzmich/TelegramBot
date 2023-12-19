package com.kuzmich.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private static final String TEXT_MESSAGE_UPDATE = "text_message_update";
    private static final String DOC_MESSAGE_UPDATE = "doc_message_update";
    private static final String PHOTO_MESSAGE_UPDATE = "photo_message_update";
    private static final String DICE_MESSAGE_UPDATE = "dice_message_update";

    private final MainService mainService;

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.info("NODE: TEXT message is received");
        mainService.processTextMessage(update);

    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
        log.info("NODE: DOC message is received");
        mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.info("NODE: PHOTO message is received");
        mainService.processPhotoMessage(update);

    }

    @Override
    @RabbitListener(queues = DICE_MESSAGE_UPDATE)
    public void consumeDiceMessageUpdates(Update update) {
        log.info("NODE: DICE message is received");
        mainService.processDiceMessage(update);
    }

}
