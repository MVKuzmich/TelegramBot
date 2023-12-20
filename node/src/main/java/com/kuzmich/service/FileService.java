package com.kuzmich.service;

import com.kuzmich.entity.AppDocument;
import com.kuzmich.entity.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
}
