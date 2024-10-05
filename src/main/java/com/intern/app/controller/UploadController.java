package com.intern.app.controller;

import com.intern.app.services.interfaces.IUploadService;
import org.springframework.http.MediaType;
import com.intern.app.exception.AppException;
import com.intern.app.services.implement.UploadService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class UploadController {
    IUploadService uploadService;

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> GetFile(@PathVariable String fileName) throws AppException {

        byte[] file = uploadService.GetFileFromFilename(fileName);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(file);
    }
}
