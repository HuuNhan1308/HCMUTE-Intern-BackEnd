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
    Long studentId;
    String year;
    Boolean isSeekingIntern = false;
    Date dob;
    String classId;

    ProfileResponse profile;
    FacultyResponse faculty;
}
