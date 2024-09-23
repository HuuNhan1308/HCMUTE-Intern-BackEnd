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
public class StudentCreationRequest {
    String studentId;
    String year;
    Boolean isSeekingIntern = false;
    Date dob;
    String classId;
    ProfileCreationRequest profile;
}
