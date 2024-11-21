package com.intern.app.controller;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Avatar;
import com.intern.app.services.interfaces.IAvatarService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/avatar")
@AllArgsConstructor
public class AvatarController {
    private final IAvatarService avatarService;

    @GetMapping("/{ownerId}")
    public ResponseEntity<byte[]> GetAvatar(@PathVariable String ownerId) {
        ReturnResult<Avatar> result = avatarService.GetAvatarByOwnerId(ownerId);
        Avatar avatar = result.getResult();

        byte[] file = avatar.getFileData();

        return switch (avatar.getFileType()) {
            case "image/jpg" -> ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
            case "image/png" -> ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(file);
            default -> ResponseEntity.ok().body(null);
        };
    }

    @PostMapping("/UploadImage")
    public ResponseEntity<ReturnResult<Boolean>> UploadCV(@RequestParam("imageFile") MultipartFile imageFile) {
        ReturnResult<Boolean> result = avatarService.UploadImage(imageFile);

        return ResponseEntity.ok().body(result);
    }
    
}
