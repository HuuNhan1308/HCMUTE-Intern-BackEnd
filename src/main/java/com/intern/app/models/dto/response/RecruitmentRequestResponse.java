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
    RequestStatus businessStatus;
    String messageToBusiness;

    StudentResponse student;
    RecruitmentResponse recruitment;
}
