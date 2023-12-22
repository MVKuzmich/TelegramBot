package com.kuzmich.service;

import com.kuzmich.entity.AppDocument;
import com.kuzmich.entity.AppPhoto;
import com.kuzmich.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocument(String documentId);
    AppPhoto getPhoto(String photoId);
}
