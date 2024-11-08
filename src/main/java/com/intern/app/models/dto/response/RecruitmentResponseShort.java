package com.intern.app.models.dto.response;

import com.intern.app.models.enums.RecruitmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecruitmentResponseShort {
    String recruitmentId;
    String title;
    String type;
    String workingDay;
    String workingHour;
    String location;
    String businessName;
    RecruitmentStatus status;
}
