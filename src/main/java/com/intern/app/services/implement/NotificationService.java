package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.NotificationMapper;
import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.response.NotificationResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Notification;
import com.intern.app.models.entity.Profile;
import com.intern.app.repository.NotificationRepository;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.services.interfaces.INotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService implements INotificationService {

   NotificationMapper notificationMapper;
   NotificationRepository notificationRepository;
   ProfileRepository profileRepository;

    @Override
    public ReturnResult<Boolean> SaveNotification(NotificationRequest notificationRequest) {
        var result = new ReturnResult<Boolean>();

        if(notificationRequest.getNotificationId() == null) {
            Notification notification = notificationMapper.toNotification(notificationRequest);
            Profile owner = profileRepository.findById(notificationRequest.getOwnerId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            Profile profile = profileRepository.findById(notificationRequest.getProfileId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            notification.setOwner(owner);
            notification.setProfile(profile);

            notificationRepository.save(notification);
        }
        else {
            Notification notification = notificationRepository.findById(notificationRequest.getNotificationId())
                    .orElseThrow(() ->  new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

            notificationMapper.updateNotification(notification, notificationRequest);
            notificationRepository.save(notification);
        }

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }

    @Override
    public ReturnResult<List<NotificationResponse>> GetUserNotifications() {
        var result = new ReturnResult<List<NotificationResponse>>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Notification> notifications = notificationRepository.findByProfileProfileIdAndReadFalse(profile.getProfileId());

        var notificationResponses = notifications.stream().map(notificationMapper::toNotificationResponse).toList();

        result.setResult(notificationResponses);
        result.setCode(200);

        return result;
    }

    @Override
    public ReturnResult<Boolean> MarkAsRead(String notificationId) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if(!notification.getProfile().getUsername().equals(username)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if(notification.getRead()) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

        notificationRepository.save(notification);
        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }
}
