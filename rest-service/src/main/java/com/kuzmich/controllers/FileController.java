package com.kuzmich.controllers;

import com.kuzmich.entity.AppDocument;
import com.kuzmich.entity.AppPhoto;
import com.kuzmich.entity.BinaryContent;
import com.kuzmich.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/document")
    public ResponseEntity<?> getDocument(@RequestParam("id") String id) {
        AppDocument document = fileService.getDocument(id);
        //TODO реализовать обработку ошибок с помощью ControllerAdvice
        if(document == null) {
            return ResponseEntity.badRequest().build();
        }
        BinaryContent binaryContent = document.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if(fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(document.getMimeType()))
                .header("Content-disposition", "attachment; filename=" + document.getDocName())
                .body(fileSystemResource);
    }
    @GetMapping("/photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        AppPhoto photo = fileService.getPhoto(id);
        //TODO реализовать обработку ошибок с помощью ControllerAdvice
        if(photo == null) {
            return ResponseEntity.badRequest().build();
        }
        BinaryContent binaryContent = photo.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if(fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);
    }
}
