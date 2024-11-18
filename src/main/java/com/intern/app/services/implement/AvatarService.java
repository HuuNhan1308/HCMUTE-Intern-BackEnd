package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Avatar;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.UploadContent;
import com.intern.app.repository.AvatarRepository;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.services.interfaces.IAvatarService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AvatarService implements IAvatarService {


    private final ProfileRepository profileRepository;
    private final AvatarRepository avatarRepository;

    @Override
    public ReturnResult<Boolean> UploadImage(MultipartFile imgFile) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        if (imgFile.getOriginalFilename() == null) {
            throw new AppException(ErrorCode.INVALID_FILE);
        }

        Profile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Avatar avatar = avatarRepository.findByOwnerId(profile.getProfileId()).orElse(null);


        try {
            // If profile is a student, and already have file. Replace it.
            if (avatar != null) {
                avatar.setFileData(imgFile.getBytes());
                avatar.setFileName(imgFile.getOriginalFilename());
            } else {
                avatar = Avatar.builder()
                        .fileName(imgFile.getOriginalFilename())
                        .fileData(imgFile.getBytes())
                        .ownerId(profile.getProfileId())
                        .fileType(imgFile.getContentType())
                        .build();
            }

            avatarRepository.save(avatar);

            result.setResult(true);
            result.setCode(200);

            return result;
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public ReturnResult<Avatar> GetAvatarByOwnerId(String ownerId) {
        var result = new ReturnResult<Avatar>();

        Profile profile = profileRepository.findById(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Avatar avatar = avatarRepository.findByOwnerId(profile.getProfileId())
                .orElseThrow(() -> new AppException(ErrorCode.AVATAR_NOT_EXISTED));

        result.setResult(avatar);
        result.setCode(200);

        return result;
    }
}
