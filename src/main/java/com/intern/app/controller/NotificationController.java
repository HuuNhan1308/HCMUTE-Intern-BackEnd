package com.intern.app.controller;

import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.response.NotificationResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.interfaces.INotificationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationController {
    INotificationService notificationService;

    @GetMapping("/GetUserNotifications")
    public ResponseEntity<ReturnResult<List<NotificationResponse>>> GetUserNotifications() {
        ReturnResult<List<NotificationResponse>> result = this.notificationService.GetUserNotifications();

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/MarkAsRead")
    public ResponseEntity<ReturnResult<Boolean>> MarkAsRead(@RequestParam String notificationId) {
        ReturnResult<Boolean> result = this.notificationService.MarkAsRead(notificationId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetUserNotificationPaging")
    public ResponseEntity<ReturnResult<PagedData<NotificationResponse, PageConfig>>> GetUserNotifications(@RequestBody PageConfig pageConfig) {
        ReturnResult<PagedData<NotificationResponse, PageConfig>> result = this.notificationService.GetUserNotificationPaging(pageConfig);

        return ResponseEntity.ok().body(result);
    }


    @GetMapping("/GetQuantityOfNotification")
    public ResponseEntity<ReturnResult<Integer>> GetQuantityOfNotification() {
        ReturnResult<Integer> result = this.notificationService.GetQuantityOfNotification();

        return ResponseEntity.ok().body(result);
    }
}
