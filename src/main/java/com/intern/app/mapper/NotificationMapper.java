package com.intern.app.mapper;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.response.NotificationResponse;
import com.intern.app.models.entity.Notification;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);

    Notification toNotification(NotificationRequest notificationRequest);

    @Mappings({
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "profile", ignore = true)
    })
    void updateNotification(@MappingTarget Notification notification, NotificationRequest notificationRequest);
}
