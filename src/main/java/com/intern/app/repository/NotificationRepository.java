package com.intern.app.repository;

import com.intern.app.models.entity.Notification;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NotificationRepository extends AppRepository<Notification, String>, JpaSpecificationExecutor<Notification> {
    List<Notification> findByProfileProfileIdAndReadFalse(String profileId);
    Notification findByNotificationId(String notificationId);
}
