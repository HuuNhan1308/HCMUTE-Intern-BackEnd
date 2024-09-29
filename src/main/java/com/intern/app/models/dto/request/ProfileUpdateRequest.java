package com.intern.app.models.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequest {
    String profileId;
    Boolean isMale;
    String bio;
    String phoneNumber;
    String email;
}
