package com.intern.app.services;

import java.util.Optional;

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
public class UploadService {
    ProfileRepository profileRepository;
    UploadContentRepository uploadContentRepository;

    public ReturnResult<Boolean> UploadCV(MultipartFile pdfFile) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        if (pdfFile.getOriginalFilename() == null) {
            throw new AppException(ErrorCode.INVALID_FILE);
        }

        Profile profile = profileRepository.findByUsernameAndDeletedFalse(username)
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
}