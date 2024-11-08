package com.intern.app.mapper;

import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.response.NotificationResponse;
import com.intern.app.models.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationRequest notificationRequest);

    NotificationResponse toNotificationResponse(Notification notification);

    @Mappings({
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "profile", ignore = true),
    })
    void updateNotification(@MappingTarget Notification notification, NotificationRequest notificationRequest);
}
