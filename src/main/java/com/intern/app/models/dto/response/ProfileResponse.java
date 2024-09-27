package com.intern.app.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    String profileId;
    String username;
    String fullname;
    Boolean isMale;
    String bio;
    String phoneNumber;
    String email;
}
