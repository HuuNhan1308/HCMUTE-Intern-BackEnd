package com.intern.app.services.implement;

import java.util.Optional;

import com.intern.app.models.entity.Notification;
import com.intern.app.services.interfaces.IAvatarService;
import com.intern.app.services.interfaces.INotificationService;
import com.intern.app.services.interfaces.IUploadService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.UploadContent;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.repository.UploadContentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadService implements IUploadService {
    ProfileRepository profileRepository;
    UploadContentRepository uploadContentRepository;

    public ReturnResult<Boolean> UploadCV(MultipartFile pdfFile) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        // Validate that the uploaded file is a PDF
        String contentType = pdfFile.getContentType();
        String originalFilename = pdfFile.getOriginalFilename().toLowerCase();

        if (!"application/pdf".equals(contentType) || !originalFilename.endsWith(".pdf")) {
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }

        // Validate file size (max 5MB = 5 * 1024 * 1024 bytes)
        long maxFileSize = 5 * 1024 * 1024; // 5 MB in bytes
        if (pdfFile.getSize() > maxFileSize) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        if (pdfFile.getOriginalFilename() == null) {
            throw new AppException(ErrorCode.INVALID_FILE);
        }

        Profile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UploadContent uploadContent = profile.getUploadContent();

        try {
            // If profile is a student, and already have file. Replace it.
            if (uploadContent != null && profile.getStudent() != null) {
                uploadContent.setFileData(pdfFile.getBytes());
                uploadContent.setFileName(pdfFile.getOriginalFilename());
            } else {
                uploadContent = UploadContent.builder()
                        .fileName(pdfFile.getOriginalFilename())
                        .fileData(pdfFile.getBytes())
                        .profile(profile)
                        .fileType(pdfFile.getContentType())
                        .build();
            }

            uploadContentRepository.save(uploadContent);

            result.setResult(true);

            return result;
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public byte[] GetFileFromFilename(String uploadContentId) {
        Optional<UploadContent> uploadContent = uploadContentRepository.findByUploadContentId(uploadContentId);

        if (uploadContent.isEmpty()) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        }

        UploadContent upload = uploadContent.get();
        return upload.getFileData();
    }

    public ReturnResult<UploadContent> GetUploadContentById(String uploadContentId) {
        var result = new ReturnResult<UploadContent>();

        UploadContent uploadContent = uploadContentRepository.findByUploadContentId(uploadContentId)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        result.setResult(uploadContent);
        result.setCode(200);

        return result;
    }
}

