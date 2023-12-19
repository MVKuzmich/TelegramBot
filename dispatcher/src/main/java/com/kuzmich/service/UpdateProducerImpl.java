package com.kuzmich.service;

import com.kuzmich.model.RabbitQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateProducerImpl implements UpdateProducer{

    private final RabbitTemplate rabbitTemplate;
    @Override
    public void produce(RabbitQueue rabbitQueue, Update update) {
        log.info(update.getMessage().getText());
        if(update.getMessage().hasDice()) {
            log.info("Dice number " + update.getMessage().getDice().getValue());
        }

        rabbitTemplate.convertAndSend(rabbitQueue.getName(), update);
    }
}
