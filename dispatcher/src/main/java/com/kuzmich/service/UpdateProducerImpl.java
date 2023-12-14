package com.kuzmich.service;

import com.kuzmich.model.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class UpdateProducerImpl implements UpdateProducer{
    @Override
    public void produce(RabbitQueue rabbitQueue, Update update) {
        log.info(update.getMessage().getText());
    }
}
