package com.intern.app.models.dto.response;

import com.intern.app.models.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    String path;
    Boolean read;

    LocalDateTime dateCreated;
}
