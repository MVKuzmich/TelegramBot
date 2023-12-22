package com.kuzmich.service;

import com.kuzmich.entity.AppDocument;
import com.kuzmich.entity.AppPhoto;
import com.kuzmich.entity.BinaryContent;
import com.kuzmich.repository.AppDocumentRepository;
import com.kuzmich.repository.AppPhotoRepository;
import com.kuzmich.utils.CryptoTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final CryptoTool cryptoTool;
    @Override
    public AppDocument getDocument(String docHashId) {
        Long id = cryptoTool.fromHash(docHashId);
        if(id == null) {
            return null;
        }
        return appDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoHashId) {
        Long id = cryptoTool.fromHash(photoHashId);
        if(id == null) {
            return null;
        }
        return appPhotoRepository.findById(id).orElse(null);
    }
}
