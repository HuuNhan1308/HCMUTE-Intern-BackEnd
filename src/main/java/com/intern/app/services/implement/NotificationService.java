package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.NotificationMapper;
import com.intern.app.models.dto.datamodel.FilterMapping;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.response.NotificationResponse;
import com.intern.app.models.dto.response.RecruitmentResponseShort;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Notification;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.enums.FilterOperator;
import com.intern.app.models.enums.FilterType;
import com.intern.app.repository.NotificationRepository;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.services.interfaces.INotificationService;
import com.intern.app.services.interfaces.IPagingService;
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
   IPagingService pagingService;

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

        notification.setRead(Boolean.TRUE);
        notificationRepository.save(notification);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }

    @Override
    public ReturnResult<PagedData<NotificationResponse, PageConfig>> GetUserNotificationPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<NotificationResponse, PageConfig>>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Clone the original PageConfig to keep the original unchanged
        PageConfig customPageConfig = PageConfig.builder()
                .pageSize(pageConfig.getPageSize())
                .currentPage(pageConfig.getCurrentPage())
                .orders(new ArrayList<>(pageConfig.getOrders()))
                .filters(new ArrayList<>(pageConfig.getFilters()))
                .build();

        List<FilterMapping> filterMappings = customPageConfig.getFilters();
        filterMappings.add(FilterMapping.builder()
                .prop("profile.profileId")
                .value(profile.getProfileId())
                .type(FilterType.TEXT)
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetNotificationPaging(customPageConfig).getResult();

        // Set data for page
        PageConfig pageConfigResult = PageConfig
                .builder()
                .pageSize(data.getPageConfig().getPageSize())
                .totalRecords(data.getPageConfig().getTotalRecords())
                .totalPage(data.getPageConfig().getTotalPage())
                .currentPage(data.getPageConfig().getCurrentPage())
                .orders(pageConfig.getOrders())
                .filters(pageConfig.getFilters())
                .build();

        // Build the PagedData object
        result.setResult(
                PagedData.<NotificationResponse, PageConfig>builder()
                        .data(data.getData())
                        .pageConfig(pageConfigResult)
                        .build()
        );

        result.setCode(200);

        return result;
    }

    @Override
    public ReturnResult<Integer> GetQuantityOfNotification() {
        var result = new ReturnResult<Integer>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Count unread notifications for the profile
        Integer quantity = notificationRepository.countByProfileProfileIdAndReadFalse(profile.getProfileId());

        result.setResult(quantity);
        return result;
    }
}
