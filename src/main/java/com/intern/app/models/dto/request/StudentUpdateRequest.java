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
    String year;
    Boolean isSeekingIntern;
    Date dob;
    String classId;

    ProfileUpdateRequest profile;

    String facultyId;
    String majorId;
}
