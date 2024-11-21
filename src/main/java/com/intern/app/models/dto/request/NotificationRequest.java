package com.intern.app.models.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    String notificationId;

    String title;
    String content;
    String path;
    @Builder.Default
    Boolean read = false;

    String ownerId;
    String profileId;
}

