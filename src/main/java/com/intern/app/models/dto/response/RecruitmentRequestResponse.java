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
public class RecruitmentRequestResponse {
    String recruitmentRequestId;
    RequestStatus businessStatus;
    String messageToBusiness;
    Double point;
    String messageToStudent;

    StudentResponse student;
    RecruitmentResponse recruitment;
}
