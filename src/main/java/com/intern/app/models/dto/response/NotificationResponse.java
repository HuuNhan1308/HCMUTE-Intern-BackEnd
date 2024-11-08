package com.intern.app.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    String notificationId;

    String title;
    String content;
    Boolean read;
}
