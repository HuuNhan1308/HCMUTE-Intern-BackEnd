package com.intern.app.models.dto.response;

import com.intern.app.models.enums.RequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InstructorRequestResponse {
    String instructorRequestId;
    StudentResponse student;
    InstructorResponse instructor;
    RequestStatus instructorStatus;
    String messageToInstructor;
    String recruitmentId;
    String recruitmentTitle;

    LocalDateTime dateCreated;
    Double point;
}
