package com.intern.app.mapper;

import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.response.NotificationResponse;
import com.intern.app.models.dto.response.UploadContentResponse;
import com.intern.app.models.entity.Notification;
import com.intern.app.models.entity.UploadContent;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UploadContentMapper {
    UploadContentResponse toUploadContentResponse(UploadContent uploadContent);
}

