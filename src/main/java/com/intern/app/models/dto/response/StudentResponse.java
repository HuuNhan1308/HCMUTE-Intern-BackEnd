package com.intern.app.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {
    String studentId;
    Integer year;
    Boolean isSeekingIntern;
    Date dob;

    ProfileResponse profile;
    MajorResponse major;
}
