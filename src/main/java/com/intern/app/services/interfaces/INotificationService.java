package com.intern.app.services.interfaces;

import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.response.NotificationResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Notification;

import java.util.List;

public interface INotificationService {
    ReturnResult<Boolean> SaveNotification(NotificationRequest notification);
    ReturnResult<List<NotificationResponse>> GetUserNotifications();
    ReturnResult<Boolean> MarkAsRead(String notificationId);
}