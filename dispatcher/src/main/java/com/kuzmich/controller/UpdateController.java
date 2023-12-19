package com.kuzmich.controller;

import com.kuzmich.model.RabbitQueue;
import com.kuzmich.service.UpdateProducer;
import com.kuzmich.service.UpdateProducerImpl;
import com.kuzmich.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.kuzmich.model.RabbitQueue.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateController {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessageByType(update); // только обработка первоначальных сообщений из приватных чатов, остальные - ошибка
        } else {
            log.error("Received unsupported message type " + update);
        }
    }

    /*
    text, doc, photo
     */
    private void distributeMessageByType(Update update) {
        Message receivedMessage = update.getMessage();
        if (receivedMessage.hasText()) {
            processTextMessage(update);
        } else if (receivedMessage.hasDocument()) {
            processDocumentMessage(update);
        } else if (receivedMessage.hasPhoto()) {
            processPhotoMessage(update);
        } else if (receivedMessage.hasDice()) {
            processDiceMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update, "Unsupported message type");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update, "Photo is received and processed");
    }

    private void processDocumentMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update, "Document is received and processed");
    }
    private void processDiceMessage(Update update) {
        updateProducer.produce(DICE_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update, "DICE is received and processed");
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void setFileIsReceivedView(Update update, String message) {
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update, message);
        setView(sendMessage);
    }
}
