package com.intern.app.services.interfaces;

import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Notification;
import com.intern.app.models.entity.UploadContent;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    ReturnResult<Boolean> UploadCV(MultipartFile pdfFile);
    byte[] GetFileFromFilename(String uploadContentId);
    ReturnResult<UploadContent> GetUploadContentById(String uploadContentId);
}

