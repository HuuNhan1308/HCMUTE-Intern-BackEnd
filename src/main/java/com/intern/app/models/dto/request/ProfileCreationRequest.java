package com.intern.app.models.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String fullname;
    Boolean isMale;
    String bio;
    String username;
    String password;
    String phoneNumber;
    String email;
}
