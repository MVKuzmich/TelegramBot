package com.kuzmich.service;

import com.kuzmich.model.RabbitQueue;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {

    void produce(RabbitQueue rabbitQueue, Update update);
}
