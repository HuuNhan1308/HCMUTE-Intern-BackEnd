package com.intern.app.models.dto.request;


import com.intern.app.models.enums.RecruitmentRequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecruitmentRequestCreationRequest {

    RecruitmentRequestStatus businessStatus;
    RecruitmentRequestStatus instructorStatus;
    String messageToBusiness;
    String messageToInstructor;

    String instructorId;
    String recruitmentId;
}
