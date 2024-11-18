package com.intern.app.services.interfaces;

import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Avatar;
import org.springframework.web.multipart.MultipartFile;

public interface IAvatarService {
    ReturnResult<Boolean> UploadImage(MultipartFile imgFile);
    ReturnResult<Avatar> GetAvatarByOwnerId(String ownerId);
}
