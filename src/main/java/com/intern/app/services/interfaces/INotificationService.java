package com.intern.app.services.interfaces;

import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Notification;

public interface INotificationService {
    ReturnResult<Boolean> SaveNotification(NotificationRequest notification);
}
