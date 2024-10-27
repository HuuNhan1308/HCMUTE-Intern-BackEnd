package com.intern.app.models.dto.response;

import com.intern.app.models.enums.RequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InstructorRequestResponse {
    StudentResponse student;
    InstructorResponse instructor;
    RequestStatus instructorStatus;
    String messageToInstructor;
}
