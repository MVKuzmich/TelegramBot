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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/document")
    public void getDocument(@RequestParam("id") String id, HttpServletResponse response) {
        AppDocument document = fileService.getDocument(id);
        //TODO реализовать обработку ошибок с помощью ControllerAdvice
        if(document == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.parseMediaType(document.getMimeType()).toString());
        response.setHeader("Content-disposition", "attachment; filename=" + document.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);

        BinaryContent binaryContent = document.getBinaryContent();
        try(ServletOutputStream os = response.getOutputStream()) {
            os.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/photo")
    public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
        AppPhoto photo = fileService.getPhoto(id);
        //TODO реализовать обработку ошибок с помощью ControllerAdvice
        if(photo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.IMAGE_JPEG.toString());
        response.setHeader("Content-disposition", "attachment;");
        response.setStatus(HttpServletResponse.SC_OK);

        BinaryContent binaryContent = photo.getBinaryContent();

        try(ServletOutputStream os = response.getOutputStream()) {
            os.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
