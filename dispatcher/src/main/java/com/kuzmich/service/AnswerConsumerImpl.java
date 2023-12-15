package com.kuzmich.service;

import com.kuzmich.controller.UpdateController;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
@Service
@RequiredArgsConstructor
public class AnswerConsumerImpl implements AnswerConsumer {
    private static final String ANSWER_MESSAGE = "answer_message";

    private final UpdateController updateController;
    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);
    }
}
