package com.kuzmich.service;

import com.kuzmich.entity.AppDocument;
import com.kuzmich.entity.AppPhoto;
import com.kuzmich.enums.PathType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);

    String generateDownloadLink(Long fileId, PathType path);
}
