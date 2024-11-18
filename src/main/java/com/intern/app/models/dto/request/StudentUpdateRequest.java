package com.intern.app.models.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentUpdateRequest {
    String studentId;
    Date dob;
    Boolean isSeekingIntern;

    ProfileUpdateRequest profile;
}
